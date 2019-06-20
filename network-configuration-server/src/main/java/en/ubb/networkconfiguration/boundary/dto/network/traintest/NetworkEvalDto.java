package en.ubb.networkconfiguration.boundary.dto.network.traintest;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.networkconfiguration.business.nonpersisted.NetworkEval;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NetworkEvalDto implements Serializable {

    @JsonProperty("accuracy")
    private double accuracy;

    @JsonProperty("precision")
    private double precision;

    @JsonProperty("f1Score")
    private double f1Score;

    @JsonProperty("recall")
    private double recall;

    @JsonProperty("previousEval")
    private NetworkEvalDto previousEval;

    public NetworkEvalDto(NetworkEval networkEval){
        this.accuracy = networkEval.getAccuracy();
        this.precision = networkEval.getPrecision();
        this.recall = networkEval.getRecall();
        this.previousEval = networkEval.getPreviousEval() == null ? null : new NetworkEvalDto(networkEval.getPreviousEval());
    }
}
