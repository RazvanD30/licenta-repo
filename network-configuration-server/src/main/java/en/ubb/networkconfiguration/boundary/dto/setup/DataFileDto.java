package en.ubb.networkconfiguration.boundary.dto.setup;

import en.ubb.networkconfiguration.domain.enums.FileType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DataFileDto {

    private Long id;

    private long networkId;

    private String classPath;

    private FileType type;

    private int nLabels;

}
