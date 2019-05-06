package en.ubb.networkconfiguration.domain.network.runtime;

import en.ubb.networkconfiguration.domain.BaseEntity;
import lombok.*;

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

    @Column(name = "class_path")
    private String classPath;

    @OneToMany(mappedBy = "dataFile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NetworkFile> networks = new ArrayList<>();

    @Builder(toBuilder = true)
    public DataFile(Long id, String classPath, List<NetworkFile> networks) {
        super(id);
        this.classPath = classPath;
        this.networks = networks;
    }
}
