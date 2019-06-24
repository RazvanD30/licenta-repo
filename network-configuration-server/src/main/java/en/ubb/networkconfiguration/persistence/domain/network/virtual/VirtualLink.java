package en.ubb.networkconfiguration.persistence.domain.network.virtual;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Link;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "virtual_links")
public class VirtualLink extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id")
    private Link link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "virtual_destination_node_id")
    private VirtualNode destinationNode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "virtual_source_node_id")
    private VirtualNode sourceNode;

    @Builder(toBuilder = true)
    public VirtualLink(Link link, VirtualNode destinationNode, VirtualNode sourceNode) {
        this.link = link;
        this.destinationNode = destinationNode;
        this.sourceNode = sourceNode;
    }
}
