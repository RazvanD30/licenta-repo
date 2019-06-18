package en.ubb.networkconfiguration.boundary.dto.setup;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DataFileDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("networkId")
    private long networkId;

    @JsonProperty("classPath")
    private String classPath;

    @JsonProperty("type")
    private FileType type;

    @JsonProperty("nLabels")
    private int nLabels;

}
