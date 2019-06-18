package en.ubb.networkconfiguration.persistence.domain.network.runtime;


import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineNode;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "nodes")
public class Node extends BaseEntity<Long> {

    @Column(name = "bias")
    private double bias;

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL)
    private List<Link> outputLinks = new ArrayList<>();

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfflineNode> offlineNodes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layer_id")
    private Layer layer;

    @Builder(toBuilder = true)
    public Node(Long id, double bias, List<Link> outputLinks, List<OfflineNode> offlineNodes, Layer layer) {
        super(id);
        this.bias = bias;
        this.outputLinks = outputLinks;
        this.offlineNodes = offlineNodes;
        this.layer = layer;
    }

    public Node(Node node){
        this.bias = node.getBias();
        this.setOutputLinks(node.getOutputLinks().stream()
                .map(link -> {
                    link = new Link(link);
                    link.setNode(this);
                    return link;
                })
                .collect(Collectors.toList()));
    }


    public void addLink(Link link) {
        this.outputLinks.add(link);
        link.setNode(this);
    }

    public boolean removeLink(Link link) {
        boolean removed = this.outputLinks.remove(link);
        if (removed) {
            link.setNode(null);
        }
        return removed;
    }

    public boolean updateLink(Link link) {
        for (int i = 0; i < this.outputLinks.size(); i++) {
            if (this.outputLinks.get(i).getId().equals(link.getId())) {
                this.outputLinks.set(i, link);
                link.setNode(this);
                return true;
            }
        }
        return false;
    }
}
