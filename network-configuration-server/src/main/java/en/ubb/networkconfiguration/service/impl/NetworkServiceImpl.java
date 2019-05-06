package en.ubb.networkconfiguration.service.impl;

import en.ubb.networkconfiguration.domain.network.runtime.*;
import en.ubb.networkconfiguration.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.repo.LayerRepo;
import en.ubb.networkconfiguration.repo.NetworkRepo;
import en.ubb.networkconfiguration.repo.NetworkStateRepo;
import en.ubb.networkconfiguration.repo.NodeRepo;
import en.ubb.networkconfiguration.service.NetworkService;
import en.ubb.networkconfiguration.util.LayerUtil;
import en.ubb.networkconfiguration.validation.exception.boundary.NetworkNotFoundException;
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

import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
public class NetworkServiceImpl implements NetworkService {

    @Autowired
    private NetworkRepo networkRepo;

    @Autowired
    private NodeRepo nodeRepo;

    @Autowired
    private LayerRepo layerRepo;

    @Autowired
    private NetworkStateRepo networkStateRepo;

    private static final Logger log = LoggerFactory.getLogger(NetworkServiceImpl.class);

    @Override
    public List<Network> getAll() {
        return networkRepo.findAll();
    }

    @Override
    public Optional<Network> getById(long id) {
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
    public Network create(@Valid NetworkInitializer initializer) throws IOException {

        Network network = this.getInitialNetwork(initializer);

        NeuralNetConfiguration.ListBuilder networkBuilder = new NeuralNetConfiguration.Builder()
                .seed(network.getSeed())
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(network.getLearningRate(), 0.9))
                .list();

        network.getLayers().forEach(layer -> {
            LayerUtil.getBuilderForType(layer.getType()).ifPresent(layerBuilder -> {
                networkBuilder.layer(layerBuilder
                        .nIn(layer.getNInputs())
                        .nOut(layer.getNNodes())
                        .activation(layer.getActivation())
                        .build());

            });
        });
        MultiLayerConfiguration conf = networkBuilder.build();
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        network.setModel(model);
        this.saveProgress(network);
        return network;
    }

    @Override
    public Network update(Network updatedNetwork) {

        Network network = networkRepo.findById(updatedNetwork.getId())
                .orElseThrow(() -> new NetworkNotFoundException(updatedNetwork.getId()))
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
    public Network run(Network network) throws IOException, InterruptedException {
        MultiLayerNetwork model = network.getModel();

        final String filenameTrain = new ClassPathResource("/classification/linear_data_train.csv").getFile().getPath();
        final String filenameTest = new ClassPathResource("/classification/linear_data_eval.csv").getFile().getPath();

        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File(filenameTrain)));
        DataSetIterator trainIter = new RecordReaderDataSetIterator(rr, network.getBatchSize(), 0, 2);

        // Load the test/evaluation data
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(new File(filenameTest)));
        DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest, network.getBatchSize(), 0, 2);

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

    }

    @Override
    public Network saveProgress(Network network) throws IOException {
        MultiLayerNetwork model = network.getModel();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
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
    }


    public void updateLink(long linkID, Link updatedLink) throws IOException {


    }

    public Network updateNode(Node updatedNode) {

        Node persistedNode = this.nodeRepo.getOne(updatedNode.getId());
        Layer persistedLayer = persistedNode.getLayer();
        Network persistedNetwork = persistedNode.getLayer().getNetwork();
        MultiLayerNetwork model = persistedNetwork.getModel();
        int layerPosition = persistedNetwork.getLayers().indexOf(persistedLayer);
        org.deeplearning4j.nn.api.Layer internalLayer = model.getLayer(layerPosition);
        int nodePosition = persistedLayer.getNodes().indexOf(persistedNode);

        String weightKey = "W";
        String biasKey = "b";

        internalLayer.getParam(biasKey).putScalar(nodePosition, updatedNode.getBias());


        for (int linkC = 0; linkC < updatedNode.getOutputLinks().size(); linkC++) {
            int[] pos = {nodePosition, linkC};
            internalLayer.getParam(weightKey).putScalar(pos, updatedNode.getOutputLinks().get(linkC).getWeight());
        }

        persistedNode.setBias(updatedNode.getBias());
        updatedNode.getOutputLinks().forEach(persistedNode::updateLink);

        return persistedNetwork;
    }

    @Override
    public Network loadNetwork(Network network) throws IOException {
        InputStream stream = new ByteArrayInputStream(network.getState().getDescriptor());
        network.setModel(ModelSerializer.restoreMultiLayerNetwork(stream, true));
        return network;
    }

    @Override
    public Network updateLayer(Layer updatedLayer) throws IOException {

        Layer persistedLayer = layerRepo.getOne(updatedLayer.getId());

        updatedLayer.getNodes().forEach(this::updateNode);

        return persistedLayer.getNetwork();
    }

    @Override
    public Network addLayer(long networkID, int position, Layer layer) {
        return null;
    }


    public MultiLayerNetwork uopdateLayer(MultiLayerNetwork model, int position, NeuralNetConfiguration layerConfiguration) {
        MultiLayerConfiguration conf = model.getLayerWiseConfigurations().clone();
        conf.getConfs().set(position, layerConfiguration);
        MultiLayerNetwork ret = new MultiLayerNetwork(conf);
        ret.init();
        return ret;
    }


    public MultiLayerNetwork aaddLayer(MultiLayerNetwork model, int position, NeuralNetConfiguration layerConfiguration) {
        MultiLayerConfiguration conf = model.getLayerWiseConfigurations().clone();
        conf.getConfs().add(position, layerConfiguration);
        MultiLayerNetwork ret = new MultiLayerNetwork(conf);
        ret.init();
        return ret;
    }
}
