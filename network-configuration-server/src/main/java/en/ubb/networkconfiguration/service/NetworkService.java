package en.ubb.networkconfiguration.service;


import en.ubb.networkconfiguration.domain.NetworkConfig;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;

public interface NetworkService {
    MultiLayerConfiguration createNetwork(NetworkConfig networkConfig);
    void runNetwork(MultiLayerConfiguration networkConfig);
}
