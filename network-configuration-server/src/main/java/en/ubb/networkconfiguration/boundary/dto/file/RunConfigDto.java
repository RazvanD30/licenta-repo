package en.ubb.networkconfiguration.boundary.dto.file;

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

    @JsonProperty("trainFileName")
    private String trainFileName;

    @JsonProperty("testFileId")
    private Long testFileId;

    @JsonProperty("testFileName")
    private String testFileName;
}
