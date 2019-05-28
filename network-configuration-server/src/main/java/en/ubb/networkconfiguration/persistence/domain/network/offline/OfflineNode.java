package en.ubb.networkconfiguration.persistence.domain.network.offline;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.enums.NodeStatusType;
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
@Table(name = "offline_nodes")
public class OfflineNode extends BaseEntity<Long> {

    @Column(name = "value")
    private double value;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private NodeStatusType status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "node_id")
    private Node node;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offline_layer_id")
    private OfflineLayer offlineLayer;

    @Builder.Default
    @OneToMany(mappedBy = "offlineNode", cascade = CascadeType.ALL)
    private List<OfflineLink> links = new ArrayList<>();

    @Builder(toBuilder = true)
    public OfflineNode(double value, NodeStatusType status, Node node, OfflineLayer offlineLayer, List<OfflineLink> links) {
        this.value = value;
        this.status = status;
        this.node = node;
        this.offlineLayer = offlineLayer;
        this.links = links;
    }
}
