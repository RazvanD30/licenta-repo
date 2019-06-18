package en.ubb.networkconfiguration.boundary.dto.runtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.networkconfiguration.persistence.domain.network.enums.LayerType;
import lombok.*;
import org.nd4j.linalg.activations.Activation;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LayerDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nInputs")
    private int nInputs;

    @JsonProperty("nNodes")
    private int nNodes;

    @JsonProperty("nOutputs")
    private int nOutputs;

    @JsonProperty("type")
    private LayerType type;

    @JsonProperty("activation")
    private Activation activation;

    @JsonProperty("nodes")
    private List<NodeDto> nodes;
}
