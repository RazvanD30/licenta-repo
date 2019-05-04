package en.ubb.networkconfiguration.domain.network.setup;

import en.ubb.networkconfiguration.domain.BaseEntity;
import en.ubb.networkconfiguration.domain.enums.LayerType;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.nd4j.linalg.activations.Activation;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "layer_initializers")
public class LayerInitializer extends BaseEntity<Long> {

    @Column(name = "nodes")
    @Range(min = 1)
    private int nNodes;

    @Column(name = "type", nullable = false)
    @NotEmpty
    private LayerType type;

    @Column(name = "activation")
    @NotEmpty
    private Activation activation;

    @Column(name = "inputs")
    @Range(min = 1)
    private int nInputs;

    @Column(name = "outputs")
    @Range(min = 1)
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
