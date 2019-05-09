package en.ubb.networkconfiguration.service.impl;

import en.ubb.networkconfiguration.domain.network.runtime.*;
import en.ubb.networkconfiguration.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.dao.*;
import en.ubb.networkconfiguration.service.NetworkService;
import en.ubb.networkconfiguration.util.LayerUtil;
import en.ubb.networkconfiguration.validation.exception.business.FileAccessBussExc;
import en.ubb.networkconfiguration.validation.exception.business.NetworkAccessBussExc;
import en.ubb.networkconfiguration.validation.exception.business.NotFoundBussExc;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.io.ClassPathResource;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
public class NetworkServiceImpl implements NetworkService {

    private final NetworkRepo networkRepo;

    private final NodeRepo nodeRepo;

    private final LayerRepo layerRepo;

    private final NetworkStateRepo networkStateRepo;

    private final LinkRepo linkRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkServiceImpl.class);

    @Autowired
    public NetworkServiceImpl(NetworkRepo networkRepo, NodeRepo nodeRepo, LayerRepo layerRepo, NetworkStateRepo networkStateRepo, LinkRepo linkRepo) {
        this.networkRepo = networkRepo;
        this.nodeRepo = nodeRepo;
        this.layerRepo = layerRepo;
        this.networkStateRepo = networkStateRepo;
        this.linkRepo = linkRepo;
    }

    @Override
    public List<Network> getAll() {
        return networkRepo.findAll();
    }

    @Override
    public Optional<Network> findById(long id) {
        return networkRepo.findById(id);
    }

    @Override
    public boolean deleteById(long id) {
        if (networkRepo.findById(id).isPresent()) {
            networkRepo.deleteById(id);
            return true;
        }
        return false;
    }

    private Network getInitialNetwork(NetworkInitializer initializer) {
        Network network = new Network().toBuilder()
                .name(initializer.getName())
                .batchSize(initializer.getBatchSize())
                .nEpochs(initializer.getNEpochs())
                .nInputs(initializer.getNInputs())
                .learningRate(initializer.getLearningRate())
                .nOutputs(initializer.getNOutputs())
                .seed(initializer.getSeed())
                .build();

        initializer.getLayers().forEach(layerInit -> {
            Layer layer = new Layer().toBuilder()
                    .activation(layerInit.getActivation())
                    .nInputs(layerInit.getNInputs())
                    .nOutputs(layerInit.getNOutputs())
                    .nNodes(layerInit.getNNodes())
                    .type(layerInit.getType())
                    .build();
            network.addLayer(layer);
        });

        for (int i = 0; i < network.getLayers().size(); i++) {
            Layer currentLayer = network.getLayers().get(i);
            for (int currentNode = 0; currentNode < currentLayer.getNNodes(); currentNode++) {
                Node node = new Node();
                for (int currentLink = 0; currentLink < currentLayer.getNOutputs(); currentLink++) {
                    Link link = new Link();
                    node.addLink(link);
                }
                currentLayer.addNode(node);
            }
        }
        return network;
    }

    @Override
    public Network create(NetworkInitializer initializer) throws NetworkAccessBussExc {

        Network network = this.getInitialNetwork(initializer);

        NeuralNetConfiguration.ListBuilder networkBuilder = new NeuralNetConfiguration.Builder()
                .seed(network.getSeed())
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(network.getLearningRate(), 0.9))
                .list();

        network.getLayers().forEach(layer -> LayerUtil.getBuilderForType(layer.getType())
                .ifPresent(layerBuilder ->
                        networkBuilder.layer(layerBuilder
                                .nIn(layer.getNInputs())
                                .nOut(layer.getNNodes())
                                .activation(layer.getActivation())
                                .build())));
        MultiLayerConfiguration conf = networkBuilder.build();
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        network.setModel(model);
        this.saveProgress(network);
        return network;
    }

    @Override
    public Network update(Network updatedNetwork) throws NotFoundBussExc {

        Network network = networkRepo.findById(updatedNetwork.getId())
                .orElseThrow(() -> new NotFoundBussExc("Network with id " + updatedNetwork.getId() + " not found"))
                .toBuilder()
                .name(updatedNetwork.getName())
                .batchSize(updatedNetwork.getBatchSize())
                .learningRate(updatedNetwork.getLearningRate())
                .nEpochs(updatedNetwork.getNEpochs())
                .nInputs(updatedNetwork.getNInputs())
                .nOutputs(updatedNetwork.getNOutputs())
                .layers(updatedNetwork.getLayers())
                .build();

        this.networkRepo.save(network);

        return network;
    }

    @Override
    public Network run(Network network, DataFile trainFile, DataFile testFile) throws FileAccessBussExc {
        try {
            MultiLayerNetwork model = network.getModel();

            final String filenameTrain = new ClassPathResource(trainFile.getClassPath()).getFile().getPath();
            final String filenameTest = new ClassPathResource(testFile.getClassPath()).getFile().getPath();

            RecordReader rr = new CSVRecordReader();
            rr.initialize(new FileSplit(new File(filenameTrain)));
            DataSetIterator trainIter =
                    new RecordReaderDataSetIterator(rr, network.getBatchSize(), 0, trainFile.getNLabels());

            // Load the test/evaluation data
            RecordReader rrTest = new CSVRecordReader();
            rrTest.initialize(new FileSplit(new File(filenameTest)));
            DataSetIterator testIter =
                    new RecordReaderDataSetIterator(rrTest, network.getBatchSize(), 0, testFile.getNLabels());

            model.setListeners(new ScoreIterationListener(10));
            model.fit(trainIter, network.getNEpochs());

            System.out.println("Evaluate model....");
            Evaluation eval = new Evaluation(network.getNOutputs());
            while (testIter.hasNext()) {
                DataSet t = testIter.next();
                INDArray features = t.getFeatures();
                INDArray lables = t.getLabels();
                INDArray predicted = model.output(features, false);

                eval.eval(lables, predicted);
            }

            //Print the evaluation statistics
            System.out.println(eval.stats());
            return network;
        } catch (IOException | InterruptedException ex) {
            throw new FileAccessBussExc("Could not access the train/test files for the network with id "
                    + network.getId() + ". " + ex.getMessage());
        }

    }

    @Override
    public Network saveProgress(Network network) throws NetworkAccessBussExc {
        MultiLayerNetwork model = network.getModel();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ModelSerializer.writeModel(model, stream, true, null);

            NetworkState state = new NetworkState();
            state.setDescriptor(stream.toByteArray());

            state = networkStateRepo.save(state);
            state.addNetwork(network);

            String weightKey = "W";
            String biasKey = "b";

            for (int layerC = 0; layerC < network.getLayers().size() - 1; layerC++) {
                Layer previousLayer = network.getLayers().get(layerC);
                Layer currentLayer = network.getLayers().get(layerC + 1);

                double[][] weights = model.getLayer(layerC).getParam(weightKey).toDoubleMatrix();
                double[] biases = model.getLayer(layerC).getParam(biasKey).toDoubleVector();

                for (int nodeC = 0; nodeC < currentLayer.getNNodes(); nodeC++) {
                    Node node = currentLayer.getNodes().get(nodeC);
                    node.setBias(biases[nodeC]);
                }

                for (int nodeP = 0; nodeP < previousLayer.getNNodes(); nodeP++) {
                    Node node = previousLayer.getNodes().get(nodeP);
                    for (int linkC = 0; linkC < previousLayer.getNOutputs(); linkC++) {
                        Link link = node.getOutputLinks().get(linkC);
                        link.setWeight(weights[nodeP][linkC]);
                    }
                }

            }
            return networkRepo.save(network);
        } catch (IOException ex) {
            throw new NetworkAccessBussExc("Could not save the model state. " + ex.getMessage());
        }
    }

    public Node updateNode(Node updatedNode) throws NetworkAccessBussExc {

        Node persistedNode = this.nodeRepo.getOne(updatedNode.getId());
        Layer persistedLayer = persistedNode.getLayer();
        Network persistedNetwork = persistedNode.getLayer().getNetwork();
        MultiLayerNetwork model = persistedNetwork.getModel();

        int layerPosition = persistedNetwork.getLayers().indexOf(persistedLayer);
        int nodePosition = persistedLayer.getNodes().indexOf(persistedNode);

        org.deeplearning4j.nn.api.Layer internalLayer = model.getLayer(layerPosition);


        String weightKey = "W";
        String biasKey = "b";

        internalLayer.getParam(biasKey).putScalar(nodePosition, updatedNode.getBias());


        for (int linkC = 0; linkC < updatedNode.getOutputLinks().size(); linkC++) {
            int[] pos = {nodePosition, linkC};
            internalLayer.getParam(weightKey).putScalar(pos, updatedNode.getOutputLinks().get(linkC).getWeight());
        }

        persistedNode.setBias(updatedNode.getBias());
        updatedNode.getOutputLinks().forEach(persistedNode::updateLink);
        this.saveProgress(persistedNetwork);

        return persistedNode;
    }

    @Override
    public Link updateLink(Link updatedLink) throws NetworkAccessBussExc {
        Link persistedLink = this.linkRepo.getOne(updatedLink.getId());
        Node persistedNode = persistedLink.getNode();
        Layer persistedLayer = persistedNode.getLayer();
        Network persistedNetwork = persistedNode.getLayer().getNetwork();
        MultiLayerNetwork model = persistedNetwork.getModel();
        int layerPosition = persistedNetwork.getLayers().indexOf(persistedLayer);
        int linkPosition = persistedNode.getOutputLinks().indexOf(persistedLink);
        int nodePosition = persistedLayer.getNodes().indexOf(persistedNode);

        org.deeplearning4j.nn.api.Layer internalLayer = model.getLayer(layerPosition);

        int[] pos = {nodePosition, linkPosition};

        String weightKey = "W";

        internalLayer.getParam(weightKey).putScalar(pos, updatedLink.getWeight());
        persistedLink.setWeight(updatedLink.getWeight());

        this.saveProgress(persistedNetwork);

        return persistedLink;
    }

    @Override
    public Network loadNetwork(Network network) throws NetworkAccessBussExc {
        try {
            InputStream stream = new ByteArrayInputStream(network.getState().getDescriptor());
            network.setModel(ModelSerializer.restoreMultiLayerNetwork(stream, true));
            return network;
        } catch (IOException ex) {
            throw new NetworkAccessBussExc("Could not load the model state. " + ex.getMessage());
        }
    }

    @Override
    public Network updateLayer(Layer updatedLayer) throws NetworkAccessBussExc {

        Layer persistedLayer = layerRepo.getOne(updatedLayer.getId());

        for (Node node : updatedLayer.getNodes()) {
            this.updateNode(node);
        }

        return persistedLayer.getNetwork();
    }

    @Override
    public Network addLayer(long networkID, int position, Layer layer) {

        //TODO
        return null;
    }



}
