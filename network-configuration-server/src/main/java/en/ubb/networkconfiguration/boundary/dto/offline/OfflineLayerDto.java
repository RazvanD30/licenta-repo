package en.ubb.networkconfiguration.boundary.dto.offline;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.networkconfiguration.persistence.domain.network.enums.LayerType;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OfflineLayerDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("type")
    private LayerType type;

    @JsonProperty("nodes")
    private List<OfflineNodeDto> nodes;
}