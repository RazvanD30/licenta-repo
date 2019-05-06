package en.ubb.networkconfiguration.boundary.dto.runtime;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NetworkDto {

    private Long id;

    private String name;

    private int seed;

    private double learningRate;

    private int batchSize;

    private int nEpochs;

    private int nInputs;

    private int nOutputs;

    private List<LayerDto> layers;
}
