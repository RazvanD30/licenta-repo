package en.ubb.networkconfiguration.boundary.dto.network.runtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NodeDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("bias")
    private double bias;

    @JsonProperty("links")
    private List<LinkDto> links;
}
