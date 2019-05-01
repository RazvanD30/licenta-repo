package en.ubb.networkconfiguration.service.impl;

import en.ubb.networkconfiguration.domain.NetworkConfig;
import en.ubb.networkconfiguration.repo.NetworkConfigRepo;
import en.ubb.networkconfiguration.service.NetworkService;
import lombok.extern.log4j.Log4j;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.Layer;
import org.deeplearning4j.nn.conf.layers.LocallyConnected1D;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.io.ClassPathResource;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class NetworkServiceImpl implements NetworkService {

    @Autowired
    private NetworkConfigRepo networkConfigRepo;

    private static final Logger log = LoggerFactory.getLogger(NetworkServiceImpl.class);

    @Override
    public MultiLayerConfiguration createNetwork(NetworkConfig networkConfig) {
/*
        try {

            DataSetIterator mnistTrain = new MnistDataSetIterator(networkConfig.getBatchSize(),true,12345);
            DataSetIterator mnistTest = new MnistDataSetIterator(networkConfig.getBatchSize(),false,12345);

            RecordReader rr = new CSVRecordReader();
            rr.initialize(new FileSplit(new File(filenameTrain)));
            DataSetIterator trainIter = new RecordReaderDataSetIterator(rr, networkConfig.getBatchSize(), 0, 1);

            // Load the test/evaluation data
            RecordReader rrTest = new CSVRecordReader();
            rrTest.initialize(new FileSplit(new File(filenameTest)));
            DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest, networkConfig.getBatchSize(), 0, 1);

            NeuralNetConfiguration.ListBuilder networkBuilder = new NeuralNetConfiguration.Builder()
                    .seed(networkConfig.getSeed())
                    .l2(0.0005)
                    .weightInit(WeightInit.XAVIER)
                    .updater(new Nesterovs(networkConfig.getLearningRate(), 0.9))
                    .list();
            networkConfig.getLayers().forEach(layer -> {
                networkBuilder.layer(layer.getBuilder()
                        .nIn(layer.getInputsCount())
                        .nOut(layer.getHiddenNodes())
                        .activation(Activation.RELU)
                        .build());
            });
            MultiLayerConfiguration conf = networkBuilder.build();
            return this.networkConfigRepo.save(conf);

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        */
return null;
    }

    @Override
    public void runNetwork(MultiLayerConfiguration networkConfig) {
        /*
        MultiLayerNetwork model = new MultiLayerNetwork(networkConfig);
        model.init();
        model.setListeners(new ScoreIterationListener(10));

        log.info("Train model....");
        model.setListeners(new ScoreIterationListener(10)); //Print score every 10 iterations
        for( int i=0; i<nEpochs; i++ ) {
            model.fit(mnistTrain);
            log.info("*** Completed epoch {} ***", i);

            log.info("Evaluate model....");
            Evaluation eval = model.evaluate(mnistTest);
            log.info(eval.stats());
            mnistTest.reset();
        }
        log.info("****************Example finished********************");
        */
    }



}
