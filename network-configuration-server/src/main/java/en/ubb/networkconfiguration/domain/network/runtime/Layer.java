package en.ubb.networkconfiguration.domain.network.runtime;

import en.ubb.networkconfiguration.domain.BaseEntity;
import en.ubb.networkconfiguration.domain.enums.LayerType;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.nd4j.linalg.activations.Activation;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "layers")
public class Layer extends BaseEntity<Long> {

    @Column(name = "inputs", nullable = false)
    @Range(min = 1)
    private int nInputs;

    @Column(name = "nodes", nullable = false)
    @Range(min = 1)
    private int nNodes;

    @Column(name = "outputs", nullable = false)
    @Range(min = 1)
    private int nOutputs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    private Network network;

    @OneToMany(mappedBy = "layer", cascade = CascadeType.ALL)
    private List<Node> nodes = new ArrayList<>();

    @Column(name = "type", nullable = false)
    @NotEmpty
    private LayerType type;

    @Column(name = "activation")
    @NotEmpty
    private Activation activation;

    @Builder(toBuilder = true)
    public Layer(Long id, int nInputs, int nNodes, int nOutputs, Network network, List<Node> nodes, LayerType type, Activation activation) {
        super(id);
        this.nInputs = nInputs;
        this.nNodes = nNodes;
        this.nOutputs = nOutputs;
        this.network = network;
        this.nodes = nodes;
        this.type = type;
        this.activation = activation;
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
