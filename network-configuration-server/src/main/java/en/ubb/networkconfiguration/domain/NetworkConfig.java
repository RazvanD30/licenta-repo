package en.ubb.networkconfiguration.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "network_configs")
public class NetworkConfig extends BaseEntity<Long> {

    @Column(name = "seed", nullable = false)
    private int seed;

    @Column(name = "learning_rate", nullable = false)
    private double learningRate;

    @Column(name = "batch_size", nullable = false)
    private int batchSize;

    @Column(name = "epochs", nullable = false)
    private int epochs;

    @Column(name = "inputs", nullable = false)
    private int inputs;

    @Column(name = "outputs", nullable = false)
    private int outputs;

    @OneToMany(mappedBy = "networkConfig")
    private List<HiddenLayerConfig> layers = new ArrayList<>();

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getEpochs() {
        return epochs;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public int getInputs() {
        return inputs;
    }

    public void setInputs(int inputs) {
        this.inputs = inputs;
    }

    public int getOutputs() {
        return outputs;
    }

    public void setOutputs(int outputs) {
        this.outputs = outputs;
    }

    public List<HiddenLayerConfig> getLayers() {
        return layers;
    }

    public void setLayers(List<HiddenLayerConfig> layers) {
        this.layers = layers;
    }
}
