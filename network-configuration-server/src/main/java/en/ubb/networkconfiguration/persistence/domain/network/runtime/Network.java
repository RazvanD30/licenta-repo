package en.ubb.networkconfiguration.persistence.domain.network.runtime;

import en.ubb.networkconfiguration.business.util.NetworkUtil;
import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualNetwork;
import lombok.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "networks")
public class Network extends BaseEntity<Long> {

    @Column(name = "created_datetime")
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Column(name = "updated_datetime")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Column(name = "is_training")
    private boolean isTraining;

    @Column(name = "name", nullable = false, length = 64, unique = true)
    @NotEmpty
    private String name;

    @Column(name = "origin_id")
    private Long originId;

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

    @Transient
    private MultiLayerNetwork model;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //TODO , cascade = CascadeType.ALL)
    @JoinColumn(name = "state_id")
    private NetworkState state;

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private List<Layer> layers = new ArrayList<>();

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private List<VirtualNetwork> virtualNetworks = new ArrayList<>();

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NetworkFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NetworkTrainLog> networkTrainLogs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private NetworkBranch branch;

    @Builder(toBuilder = true)
    public Network(Long id, LocalDateTime createDateTime, LocalDateTime updateDateTime,
                   @NotEmpty String name, int seed, @Range(min = 0, max = 10) double learningRate,
                   @Range(min = 1) int batchSize, @Range(min = 1) int nEpochs, @Range(min = 1) int nInputs,
                   @Range(min = 1) int nOutputs, MultiLayerNetwork model, NetworkState state, List<Layer> layers,
                   List<VirtualNetwork> virtualNetworks, List<NetworkFile> files, List<NetworkTrainLog> networkTrainLogs,
                   NetworkBranch branch, Long originId, boolean isTraining) {
        super(id);
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
        this.name = name;
        this.seed = seed;
        this.learningRate = learningRate;
        this.batchSize = batchSize;
        this.nEpochs = nEpochs;
        this.nInputs = nInputs;
        this.nOutputs = nOutputs;
        this.model = model;
        this.state = state;
        this.layers = layers;
        this.virtualNetworks = virtualNetworks;
        this.files = files;
        this.networkTrainLogs = networkTrainLogs;
        this.branch = branch;
        this.originId = originId;
        this.isTraining = isTraining;
    }

    /**
     * Copy constructor, will not initialize id, branch and virtual networks.
     *
     * @param network the network from which to copy the fields recursively.
     */
    public Network(Network network) {
        this.originId = network.getId();
        this.createDateTime = network.getCreateDateTime();
        this.updateDateTime = network.getUpdateDateTime();
        this.name = network.getName();
        this.seed = network.getSeed();
        this.learningRate = network.getLearningRate();
        this.batchSize = network.getBatchSize();
        this.nEpochs = network.getNEpochs();
        this.nInputs = network.getNInputs();
        this.nOutputs = network.getNOutputs();
        this.model = network.getModel();
        this.state = new NetworkState(network.getState());
        this.state.getNetworks().add(this);
        network.getFiles().forEach(networkFile -> this.addFile(networkFile.getDataFile(), networkFile.getType()));
        this.networkTrainLogs = network.getNetworkTrainLogs();
        this.isTraining = false;
        this.setLayers(network.getLayers().stream()
                .map(layer -> {
                    layer = new Layer(layer);
                    layer.setNetwork(this);
                    return layer;
                })
                .collect(Collectors.toList()));
    }

    public MultiLayerNetwork getModel() {
        return model != null ? model : NetworkUtil.loadModel(this);
    }

    public void addNetworkTrainLog(NetworkTrainLog networkTrainLog) {
        this.networkTrainLogs.add(networkTrainLog);
        networkTrainLog.setNetwork(this);
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


    public void addOfflineNetwork(VirtualNetwork virtualNetwork) {
        this.virtualNetworks.add(virtualNetwork);
        virtualNetwork.setNetwork(this);
    }

    public boolean removeOfflineNetwork(VirtualNetwork virtualNetwork) {
        boolean removed = this.virtualNetworks.remove(virtualNetwork);
        if (removed) {
            virtualNetwork.setNetwork(null);
        }
        return removed;
    }


    public void addFile(DataFile dataFile, FileType type) {
        NetworkFile networkFile = new NetworkFile(this, dataFile, type);
        this.files.add(networkFile);
        dataFile.getNetworks().add(networkFile);
    }

    public boolean removeFile(DataFile dataFile, FileType type) {
        boolean result = false;
        for (Iterator<NetworkFile> iterator = this.files.iterator(); iterator.hasNext(); ) {
            NetworkFile networkFile = iterator.next();
            if (networkFile.getType().equals(type)
                    && networkFile.getNetwork().equals(this) && networkFile.getDataFile().equals(dataFile)) {
                iterator.remove();
                networkFile.getDataFile().getNetworks().remove(networkFile);
                networkFile.setNetwork(null);
                networkFile.setDataFile(null);
                result = true;
            }
        }
        return result;
    }

    public boolean updateFile(DataFile dataFile, FileType type) {
        for (NetworkFile networkFile : this.files) {
            if (networkFile.getNetwork().equals(this) && networkFile.getDataFile().equals(dataFile)) {
                networkFile.setType(type);
                return true;
            }
        }
        return false;
    }
}
