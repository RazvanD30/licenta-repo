package en.ubb.networkconfiguration.boundary.dto.network.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NetworkIterationLogDto {

    @JsonProperty("score")
    private double score;

    @JsonProperty("iteration")
    private int iteration;

}
