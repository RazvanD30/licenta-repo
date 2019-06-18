package en.ubb.networkconfiguration.boundary.dto.branch;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.networkconfiguration.boundary.dto.authentication.PublicUserDto;
import en.ubb.networkconfiguration.persistence.domain.network.enums.BranchType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BranchDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private BranchType type;

    @JsonProperty("owner")
    private PublicUserDto owner;

    @JsonProperty
    private List<PublicUserDto> contributors;

    @JsonProperty("created")
    private LocalDateTime createDateTime;

    @JsonProperty("updated")
    private LocalDateTime updateDateTime;

    @JsonProperty("source")
    private Long sourceId;
}
