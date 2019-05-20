package en.ubb.networkconfiguration.persistence.domain.network.setup;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "network_initializers")
public class NetworkInitializer extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    @NotEmpty
    private String name;

    @Column(name = "seed", nullable = false)
    private int seed;

    @Column(name = "learning_rate", nullable = false)
    @Range(min = 0, max = 10)
    private double learningRate;

    @Column(name = "batch_size", nullable = false)
    @Range(min = 1)
    private int batchSize;

    @Column(name = "epochs", nullable = false)
    @Range(min = 1)
    private int nEpochs;

    @Column(name = "inputs", nullable = false)
    @Range(min = 1)
    private int nInputs;

    @Column(name = "outputs", nullable = false)
    @Range(min = 1)
    private int nOutputs;

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private List<LayerInitializer> layers = new ArrayList<>();

    @Builder(toBuilder = true)
    public NetworkInitializer(Long id, String name, int seed, double learningRate, int batchSize, int nEpochs, int nInputs, int nOutputs, List<LayerInitializer> layers) {
        super(id);
        this.name = name;
        this.seed = seed;
        this.learningRate = learningRate;
        this.batchSize = batchSize;
        this.nEpochs = nEpochs;
        this.nInputs = nInputs;
        this.nOutputs = nOutputs;
        this.layers = layers;
    }

    public void addLayer(LayerInitializer layer) {
        this.layers.add(layer);
        layer.setNetwork(this);
    }

    public boolean removeLayer(LayerInitializer layer) {
        boolean removed = this.layers.remove(layer);
        if (removed) {
            layer.setNetwork(null);
        }
        return removed;
    }

    public boolean updateLayer(LayerInitializer layer) {
        for (int i = 0; i < this.layers.size(); i++) {
            if (this.layers.get(i).getId().equals(layer.getId())) {
                this.layers.set(i, layer);
                layer.setNetwork(this);
                return true;
            }
        }
        return false;
    }

}
