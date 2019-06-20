package en.ubb.networkconfiguration.business.nonpersisted;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NetworkEval implements Serializable {

    private double accuracy;

    private double precision;

    private double f1Score;

    private double recall;

    private NetworkEval previousEval;
}
