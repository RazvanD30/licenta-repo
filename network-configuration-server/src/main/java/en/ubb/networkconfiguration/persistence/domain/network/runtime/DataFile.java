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

    @Column(name = "class_path", nullable = false, unique = true, length = 100)
    private String classPath;

    @Column(name = "labels")
    @Range(min = 1)
    private int nLabels;

    @OneToMany(mappedBy = "dataFile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NetworkFile> networks = new ArrayList<>();

    @Builder(toBuilder = true)
    public DataFile(Long id, String classPath, @Range(min = 1) int nLabels, List<NetworkFile> networks) {
        super(id);
        this.classPath = classPath;
        this.nLabels = nLabels;
        this.networks = networks;
    }
}
