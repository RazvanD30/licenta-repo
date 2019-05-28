package en.ubb.networkconfiguration.persistence.domain.network.runtime;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.enums.LayerType;
import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineLayer;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.nd4j.linalg.activations.Activation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "layers")
public class Layer extends BaseEntity<Long> {

    @Column(name = "inputs", nullable = false)
    @Range(min = 0)
    private int nInputs;

    @Column(name = "nodes", nullable = false)
    @Range(min = 1)
    private int nNodes;

    @Column(name = "outputs", nullable = false)
    @Range(min = 0)
    private int nOutputs;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "network_id")
    private Network network;

    @Builder.Default
    @OneToMany(mappedBy = "layer", cascade = CascadeType.ALL)
    private List<Node> nodes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "layer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfflineLayer> offlineLayers = new ArrayList<>();

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LayerType type;

    @Column(name = "activation")
    @Enumerated(EnumType.STRING)
    private Activation activation;


    @Builder(toBuilder = true)
    public Layer(Long id, @Range(min = 0) int nInputs, @Range(min = 1) int nNodes, @Range(min = 0) int nOutputs,
                 Network network, List<Node> nodes, List<OfflineLayer> offlineLayers, LayerType type, Activation activation) {
        super(id);
        this.nInputs = nInputs;
        this.nNodes = nNodes;
        this.nOutputs = nOutputs;
        this.network = network;
        this.nodes = nodes;
        this.offlineLayers = offlineLayers;
        this.type = type;
        this.activation = activation;
    }

    public Layer(Layer layer) {
        this.nInputs = layer.getNInputs();
        this.nNodes = layer.getNNodes();
        this.nOutputs = layer.getNOutputs();
        this.type = layer.getType();
        this.activation = layer.getActivation();
        this.setNodes(layer.getNodes().stream()
                .map(node -> {
                    node = new Node(node);
                    node.setLayer(this);
                    return node;
                })
                .collect(Collectors.toList()));
    }

    public void addNode(Node node) {
        this.nodes.add(node);
        node.setLayer(this);
    }

    public boolean removeNode(Node node) {
        boolean removed = this.nodes.remove(node);
        if (removed) {
            node.setLayer(null);
        }
        return removed;
    }

    public boolean updateNode(Node node) {
        for (int i = 0; i < this.nodes.size(); i++) {
            if (this.nodes.get(i).getId().equals(node.getId())) {
                this.nodes.set(i, node);
                node.setLayer(this);
                return true;
            }
        }
        return false;
    }

}
