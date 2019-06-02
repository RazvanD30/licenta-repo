package en.ubb.networkconfiguration.persistence.domain.network.offline;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Layer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "offline_layers")
public class OfflineLayer extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layer_id")
    private Layer layer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offline_network_id")
    private OfflineNetwork offlineNetwork;

    @OneToMany(mappedBy = "offlineLayer", cascade = CascadeType.ALL)
    private List<OfflineNode> nodes = new ArrayList<>();

    @Builder(toBuilder = true)
    public OfflineLayer(Layer layer, OfflineNetwork offlineNetwork, List<OfflineNode> nodes) {
        this.layer = layer;
        this.offlineNetwork = offlineNetwork;
        this.nodes = nodes;
    }
}
