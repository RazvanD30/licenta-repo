package en.ubb.networkconfiguration.persistence.domain.network.runtime;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "network_iteration_logs")
public class NetworkIterationLog extends BaseEntity<Long> {

    @Column(name = "score")
    private double score;

    @Column(name = "iteration")
    private int iteration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_train_log_id")
    private NetworkTrainLog networkTrainLog;

    @Builder(toBuilder = true)
    public NetworkIterationLog(Long id, double score, int iteration, NetworkTrainLog networkTrainLog) {
        super(id);
        this.score = score;
        this.iteration = iteration;
        this.networkTrainLog = networkTrainLog;
    }
}
