package en.ubb.networkconfiguration.persistence.domain.network.virtual;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.enums.VirtualNodeStatus;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Node;
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
@Table(name = "virtual_nodes")
public class VirtualNode extends BaseEntity<Long> {

    @Column(name = "value")
    private Double value;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VirtualNodeStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private Node node;

    @Column(name = "position")
    private int position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "virtual_layer_id")
    private VirtualLayer virtualLayer;

    @OneToMany(mappedBy = "sourceNode", cascade = CascadeType.ALL)
    private List<VirtualLink> outputLinks = new ArrayList<>();

    @OneToMany(mappedBy = "destinationNode", cascade = CascadeType.ALL)
    private List<VirtualLink> inputsLinks = new ArrayList<>();

    @Builder(toBuilder = true)
    public VirtualNode(Long id, Double value, VirtualNodeStatus status, Node node, int position,
                       VirtualLayer virtualLayer, List<VirtualLink> outputLinks, List<VirtualLink> inputsLinks ) {
        super(id);
        this.value = value;
        this.status = status;
        this.node = node;
        this.position = position;
        this.virtualLayer = virtualLayer;
        this.outputLinks = outputLinks;
        this.inputsLinks = inputsLinks;
    }

    public void addOutputLink(VirtualLink link){
        this.outputLinks.add(link);
        link.setSourceNode(this);
    }

    public void addInputLink(VirtualLink link){
        this.inputsLinks.add(link);
        link.setDestinationNode(this);
    }
}
