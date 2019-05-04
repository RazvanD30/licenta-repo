package en.ubb.networkconfiguration.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LinkDto {

    private long id;

    private double weight;
}
