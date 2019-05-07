package en.ubb.networkconfiguration.boundary.dto.runtime;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LinkDto {

    private Long id;

    private double weight;
}
