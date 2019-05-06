package en.ubb.networkconfiguration.domain.network.runtime;

import en.ubb.networkconfiguration.domain.enums.FileType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "networks_files")
public class NetworkFile {

    @EmbeddedId
    private NetworkFileId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("networkId")
    private Network network;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("fileId")
    private DataFile dataFile;

    @Column(name = "type")
    private FileType type;

    public NetworkFile(Network network, DataFile dataFile) {
        this.network = network;
        this.dataFile = dataFile;
        this.id = new NetworkFileId(network.getId(), dataFile.getId());
    }

    public NetworkFile(Network network, DataFile dataFile, FileType type) {
        this.network = network;
        this.dataFile = dataFile;
        this.id = new NetworkFileId(network.getId(), dataFile.getId());
        this.type = type;
    }

}
