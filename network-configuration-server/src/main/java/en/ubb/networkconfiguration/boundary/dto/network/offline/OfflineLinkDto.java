package en.ubb.networkconfiguration.boundary.dto.network.offline;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OfflineLinkDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("weight")
    private double weight;

}
