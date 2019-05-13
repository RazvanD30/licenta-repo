package en.ubb.networkconfiguration.business.util;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NetworkUtil {

    public static MultiLayerNetwork loadModel(Network network){
        try {
            InputStream stream = new ByteArrayInputStream(network.getState().getDescriptor());
            MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(stream, true);
            network.setModel(model);
            return model;
        } catch (IOException e){
            return null;
        }
    }
}
