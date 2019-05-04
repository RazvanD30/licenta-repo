package en.ubb.networkconfiguration.util;

import en.ubb.networkconfiguration.domain.enums.LayerType;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.FeedForwardLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.Optional;

public class LayerUtil {


    public static Optional<FeedForwardLayer.Builder> getBuilderForType(LayerType type) {

        switch (type) {
            case INPUT:
                return Optional.empty();
            case OUTPUT:
                return Optional.of(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD));
            case DENSE:
                return Optional.of(new DenseLayer.Builder());
            default:
                return Optional.empty();
        }

    }
}
