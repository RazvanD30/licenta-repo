package en.ubb.networkconfiguration.boundary.dto.offline;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.networkconfiguration.persistence.domain.network.enums.NodeStatusType;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OfflineNodeDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("bias")
    private double bias;

    @JsonProperty("value")
    private double value;

    private NodeStatusType status;

    @JsonProperty("links")
    private List<OfflineLinkDto> links;
}
