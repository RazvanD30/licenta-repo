package en.ubb.networkconfiguration.domain.network.setup;

import en.ubb.networkconfiguration.domain.BaseEntity;
import en.ubb.networkconfiguration.domain.enums.LayerType;
import lombok.*;
import org.nd4j.linalg.activations.Activation;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "layer_initializers")
public class LayerInitializer extends BaseEntity<Long> {

    @Column(name = "nodes")
    private int nNodes;

    @Column(name = "type", nullable = false)
    private LayerType type;

    @Column(name = "activation")
    private Activation activation;

    @Column(name = "inputs")
    private int nInputs;

    @Column(name = "outputs")
    private int nOutputs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    private NetworkInitializer network;

    @Builder(toBuilder = true)
    public LayerInitializer(Long id, int nNodes, LayerType type, Activation activation, int nInputs, int nOutputs, NetworkInitializer network) {
        super(id);
        this.nNodes = nNodes;
        this.type = type;
        this.activation = activation;
        this.nInputs = nInputs;
        this.nOutputs = nOutputs;
        this.network = network;
    }
}
