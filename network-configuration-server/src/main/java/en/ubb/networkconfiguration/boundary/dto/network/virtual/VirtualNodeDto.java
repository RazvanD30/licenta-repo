package en.ubb.networkconfiguration.boundary.dto.network.virtual;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.networkconfiguration.persistence.domain.network.enums.VirtualNodeStatus;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VirtualNodeDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("bias")
    private double bias;

    @JsonProperty("value")
    private Double value;

    @JsonProperty("position")
    private int position;

    @JsonProperty("status")
    private VirtualNodeStatus status;

    @JsonProperty("outputLinks")
    private List<VirtualLinkDto> outputLinks;

    @JsonProperty("inputLinks")
    private List<VirtualLinkDto> inputLinks;
}
