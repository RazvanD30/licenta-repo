package en.ubb.networkconfiguration.boundary.dto.runtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NetworkDto {

    @JsonProperty("id")
    private Long id;

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

    @JsonProperty("layers")
    private List<LayerDto> layers;
}
