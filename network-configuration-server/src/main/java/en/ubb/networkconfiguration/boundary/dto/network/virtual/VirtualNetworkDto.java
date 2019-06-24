package en.ubb.networkconfiguration.boundary.dto.network.virtual;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VirtualNetworkDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("networkName")
    private String networkName;

    @JsonProperty
    private Long networkId;

    @JsonProperty("nLayers")
    private Integer layerCount;

}
