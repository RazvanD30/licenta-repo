package en.ubb.networkconfiguration.boundary.dto.runtime;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RunConfigDto {

    private Long networkId;

    private Long trainFileId;

    private Long testFileId;
}
