package en.ubb.networkconfiguration.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NodeDto {

    private long id;

    private double bias;

    private List<LinkDto> links;
}
