package en.ubb.networkconfiguration.dto.setup;

import en.ubb.networkconfiguration.domain.enums.LayerType;
import en.ubb.networkconfiguration.dto.NodeDto;
import lombok.*;
import org.nd4j.linalg.activations.Activation;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LayerInitDto {

    private long id;

    private int nInputs;

    private int nNodes;

    private int nOutputs;

    private LayerType type;

    private Activation activation;

}