package en.ubb.networkconfiguration.persistence.domain.network.virtual;

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
@Table(name = "virtual_networks")
public class VirtualNetwork extends BaseEntity<Long> {

    @Column(name = "created_datetime")
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    private Network network;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "virtualNetwork", cascade = CascadeType.ALL)
    private List<VirtualLayer> layers = new ArrayList<>();

    @Builder(toBuilder = true)
    public VirtualNetwork(LocalDateTime createDateTime, Network network, String name, List<VirtualLayer> layers) {
        this.createDateTime = createDateTime;
        this.network = network;
        this.name = name;
        this.layers = layers;
    }

    public void addLayer(VirtualLayer layer){
        this.layers.add(layer);
        layer.setVirtualNetwork(this);
    }
}
