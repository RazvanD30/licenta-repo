package en.ubb.networkconfiguration.persistence.domain.network.runtime;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "network_train_logs")
public class NetworkTrainLog extends BaseEntity<Long> {

    @Column(name = "created_datetime")
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Column(name = "accuracy")
    private double accuracy;

    @Column(name = "prec")
    private double precision;

    @Column(name = "recall")
    private double recall;

    @Column(name = "f1_score")
    private double f1Score;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "network_id")
    private Network network;

    @Builder.Default
    @OneToMany(mappedBy = "networkTrainLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NetworkIterationLog> networkIterationLogs = new ArrayList<>();

    @Builder(toBuilder = true)
    public NetworkTrainLog(Long id, LocalDateTime createDateTime, double accuracy, double precision, double recall,
                           double f1Score, Network network, List<NetworkIterationLog> networkIterationLogs) {
        super(id);
        this.createDateTime = createDateTime;
        this.accuracy = accuracy;
        this.precision = precision;
        this.recall = recall;
        this.f1Score = f1Score;
        this.network = network;
        this.networkIterationLogs = networkIterationLogs;
    }




    public void addNetworkIterationLog(NetworkIterationLog networkIterationLog){
        this.networkIterationLogs.add(networkIterationLog);
        networkIterationLog.setNetworkTrainLog(this);
    }
}
