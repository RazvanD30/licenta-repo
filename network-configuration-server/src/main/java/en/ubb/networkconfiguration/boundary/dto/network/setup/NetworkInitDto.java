package en.ubb.networkconfiguration.boundary.dto.network.setup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NetworkInitDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("seed")
    private int seed;

    @JsonProperty("learningRate")
    private double learningRate;

    @JsonProperty("batchSize")
    private int batchSize;

    @JsonProperty("nEpochs")
    private int nEpochs;

    @JsonProperty("nInputs")
    private int nInputs;

    @JsonProperty("nOutputs")
    private int nOutputs;

    @JsonProperty
    private Long branchId;

    @JsonProperty("layers")
    private List<LayerInitDto> layers;
}
