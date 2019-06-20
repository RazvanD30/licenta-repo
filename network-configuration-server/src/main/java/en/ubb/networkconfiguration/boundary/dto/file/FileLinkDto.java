package en.ubb.networkconfiguration.boundary.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FileLinkDto {

    @JsonProperty("networkId")
    private long networkId;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("fileType")
    private FileType fileType;
}
