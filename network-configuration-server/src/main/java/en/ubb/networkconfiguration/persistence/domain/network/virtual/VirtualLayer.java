package en.ubb.networkconfiguration.persistence.domain.network.virtual;

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
@Table(name = "virtual_layers")
public class VirtualLayer extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layer_id")
    private Layer layer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "virtual_network_id")
    private VirtualNetwork virtualNetwork;

    @OneToMany(mappedBy = "virtualLayer", cascade = CascadeType.ALL)
    private List<VirtualNode> nodes = new ArrayList<>();

    @Builder(toBuilder = true)
    public VirtualLayer(Layer layer, VirtualNetwork virtualNetwork, List<VirtualNode> nodes) {
        this.layer = layer;
        this.virtualNetwork = virtualNetwork;
        this.nodes = nodes;
    }

    public void addNode(VirtualNode node){
        this.nodes.add(node);
        node.setVirtualLayer(this);
    }
}
