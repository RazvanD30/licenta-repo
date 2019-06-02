package en.ubb.networkconfiguration.persistence.domain.network.runtime;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "networks_files")
public class NetworkFile extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    private Network network;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "file_id")
    private DataFile dataFile;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType type;

    @Builder(toBuilder = true)
    public NetworkFile(Network network, DataFile dataFile, FileType type) {
        this.network = network;
        this.dataFile = dataFile;
        this.type = type;
    }
}
