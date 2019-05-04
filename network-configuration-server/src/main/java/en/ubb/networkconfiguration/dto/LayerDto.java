package en.ubb.networkconfiguration.dto;

import en.ubb.networkconfiguration.domain.enums.LayerType;
import lombok.*;
import org.nd4j.linalg.activations.Activation;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LayerDto {

    private long id;

    private int nInputs;

    private int nNodes;

    private int nOutputs;

    private LayerType type;

    private Activation activation;

    private List<NodeDto> nodes;
}
