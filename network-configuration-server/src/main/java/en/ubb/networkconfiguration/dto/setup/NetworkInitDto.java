package en.ubb.networkconfiguration.dto.setup;

import en.ubb.networkconfiguration.dto.LayerDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NetworkInitDto {

    private long id;

    private String name;

    private int seed;

    private double learningRate;

    private int batchSize;

    private int nEpochs;

    private int nInputs;

    private int nOutputs;

    private List<LayerInitDto> layers;
}
