package en.ubb.networkconfiguration.boundary.dto.runtime;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NodeDto {

    private Long id;

    private double bias;

    private List<LinkDto> links;
}