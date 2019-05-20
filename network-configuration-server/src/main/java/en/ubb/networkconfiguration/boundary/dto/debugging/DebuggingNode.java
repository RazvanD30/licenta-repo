package en.ubb.networkconfiguration.boundary.dto.debugging;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DebuggingNode {
    private double bias;
    private Map<DebuggingNode, Double> outputLinks = new HashMap<>();
    private NodeStatus status;
    private Double value;
}
