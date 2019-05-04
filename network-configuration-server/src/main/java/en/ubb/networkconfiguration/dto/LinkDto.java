package en.ubb.networkconfiguration.dto;

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
