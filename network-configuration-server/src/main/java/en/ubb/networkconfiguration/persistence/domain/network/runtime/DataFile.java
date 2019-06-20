package en.ubb.networkconfiguration.persistence.domain.network.runtime;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "data_files")
public class DataFile extends BaseEntity<Long> {

    @Column(name = "filename", nullable = false, unique = true, length = 64)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "labels")
    @Range(min = 1)
    private int nLabels;

    @OneToMany(mappedBy = "dataFile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NetworkFile> networks = new ArrayList<>();

    @Builder(toBuilder = true)
    public DataFile(Long id, String name, String type, byte[] data, @Range(min = 1) int nLabels, List<NetworkFile> networks) {
        super(id);
        this.name = name;
        this.type = type;
        this.data = data;
        this.nLabels = nLabels;
        this.networks = networks;
    }
}
