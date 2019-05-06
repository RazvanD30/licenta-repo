package en.ubb.networkconfiguration.domain.network.runtime;

import en.ubb.networkconfiguration.domain.BaseEntity;
import en.ubb.networkconfiguration.util.NetworkUtil;
import lombok.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "networks")
public class Network extends BaseEntity<Long> {

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "state_id")
    private NetworkState state;

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private List<Layer> layers = new ArrayList<>();

    @Transient
    private MultiLayerNetwork model;

    @Builder(toBuilder = true)
    public Network(Long id, @NotEmpty String name, int seed, @Range(min = 0, max = 10) double learningRate, @Range(min = 1) int batchSize, @Range(min = 1) int nEpochs, @Range(min = 1) int nInputs, @Range(min = 1) int nOutputs, NetworkState state, List<Layer> layers, MultiLayerNetwork model) {
        super(id);
        this.name = name;
        this.seed = seed;
        this.learningRate = learningRate;
        this.batchSize = batchSize;
        this.nEpochs = nEpochs;
        this.nInputs = nInputs;
        this.nOutputs = nOutputs;
        this.state = state;
        this.layers = layers;
        this.model = model;
    }


    public MultiLayerNetwork getModel() {
        return model != null ? model : NetworkUtil.loadModel(this);
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
        layer.setNetwork(this);
    }

    public boolean removeLayer(Layer layer) {
        boolean removed = this.layers.remove(layer);
        if (removed) {
            layer.setNetwork(null);
        }
        return removed;
    }

    public boolean updateLayer(Layer layer) {
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
