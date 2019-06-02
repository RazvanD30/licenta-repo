package en.ubb.networkconfiguration.boundary.dto.network.offline;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.networkconfiguration.persistence.domain.network.enums.OfflineNodeStatus;
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

    private OfflineNodeStatus status;

    @JsonProperty("links")
    private List<OfflineLinkDto> links;
}
