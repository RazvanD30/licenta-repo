package en.ubb.networkconfiguration.boundary.dto.network.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NetworkTrainLogDto {

    @JsonProperty("createDateTime")
    private LocalDateTime createDateTime;

    @JsonProperty("accuracy")
    private double accuracy;

    @JsonProperty("precision")
    private double precision;

    @JsonProperty("recall")
    private double recall;

    @JsonProperty("f1Score")
    private double f1Score;

    @JsonProperty("networkIterationLogs")
    private List<NetworkIterationLogDto> networkIterationLogs = new ArrayList<>();
}
