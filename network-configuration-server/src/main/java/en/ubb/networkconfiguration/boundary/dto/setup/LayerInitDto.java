package en.ubb.networkconfiguration.boundary.dto.setup;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.networkconfiguration.persistence.domain.enums.LayerType;
import lombok.*;
import org.nd4j.linalg.activations.Activation;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LayerInitDto {

    @JsonProperty("id")
    private long id;

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

}