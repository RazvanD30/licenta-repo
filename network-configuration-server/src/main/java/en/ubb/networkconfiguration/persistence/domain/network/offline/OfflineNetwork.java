package en.ubb.networkconfiguration.persistence.domain.network.offline;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "offline_networks")
public class OfflineNetwork extends BaseEntity<Long> {

    @Column(name = "created_datetime")
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    private Network network;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "offlineNetwork", cascade = CascadeType.ALL)
    private List<OfflineLayer> layers = new ArrayList<>();

    @Builder(toBuilder = true)
    public OfflineNetwork(LocalDateTime createDateTime, Network network, String name, List<OfflineLayer> layers) {
        this.createDateTime = createDateTime;
        this.network = network;
        this.name = name;
        this.layers = layers;
    }

    public void addLayer(OfflineLayer layer){
        this.layers.add(layer);
        layer.setOfflineNetwork(this);
    }
}
