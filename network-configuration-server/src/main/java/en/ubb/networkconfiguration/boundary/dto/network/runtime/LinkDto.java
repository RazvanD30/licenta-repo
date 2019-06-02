package en.ubb.networkconfiguration.boundary.dto.network.runtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LinkDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("weight")
    private double weight;
}
