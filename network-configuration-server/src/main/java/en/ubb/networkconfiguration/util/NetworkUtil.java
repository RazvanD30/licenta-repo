package en.ubb.networkconfiguration.util;

import en.ubb.networkconfiguration.domain.network.runtime.Network;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NetworkUtil {

    public static MultiLayerNetwork loadModel(Network network){
        try {
            InputStream stream = new ByteArrayInputStream(network.getNetwork());
            MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(stream, true);
            network.setModel(model);
            return model;
        } catch (IOException e){
            return null;
        }
    }
}
