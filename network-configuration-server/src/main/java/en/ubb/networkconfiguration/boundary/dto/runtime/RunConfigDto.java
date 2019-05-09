package en.ubb.networkconfiguration.boundary.dto.runtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RunConfigDto {

    @JsonProperty("networkId")
    private Long networkId;

    @JsonProperty("trainFileId")
    private Long trainFileId;

    @JsonProperty("testFileId")
    private Long testFileId;
}
