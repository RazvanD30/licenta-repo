package en.ubb.networkconfiguration.boundary.dto.network.virtual;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VirtualLinkDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("sourceNodeId")
    private Long sourceNodeId;

    @JsonProperty("destinationNodeId")
    private Long destinationNodeId;

}
