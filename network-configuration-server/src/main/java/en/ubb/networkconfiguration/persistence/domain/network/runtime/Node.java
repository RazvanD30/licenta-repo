package en.ubb.networkconfiguration.persistence.domain.network.runtime;


import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualNode;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "nodes")
public class Node extends BaseEntity<Long> {

    @Column(name = "bias")
    private double bias;

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
    private List<Link> outputLinks = new ArrayList<>();

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    private List<Link> inputLinks = new ArrayList<>();

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VirtualNode> virtualNodes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layer_id")
    private Layer layer;

    @Builder(toBuilder = true)
    public Node(Long id, double bias, List<Link> outputLinks,List<Link> inputLinks, List<VirtualNode> virtualNodes, Layer layer) {
        super(id);
        this.bias = bias;
        this.outputLinks = outputLinks;
        this.inputLinks = inputLinks;
        this.virtualNodes = virtualNodes;
        this.layer = layer;
    }

    public Node(Node node){
        this.bias = node.getBias();
    }


    public void addOutputLink(Link link) {
        this.outputLinks.add(link);
        link.setSource(this);
    }

    public void addInputLink(Link link){
        this.inputLinks.add(link);
        link.setDestination(this);
    }

    public boolean removeOutputLink(Link link) {
        boolean removed = this.outputLinks.remove(link);
        if (removed) {
            link.setSource(null);
        }
        return removed;
    }

    public boolean removeInputLink(Link link) {
        boolean removed = this.inputLinks.remove(link);
        if (removed) {
            link.setDestination(null);
        }
        return removed;
    }

    public boolean updateOutputLink(Link link) {
        for (int i = 0; i < this.outputLinks.size(); i++) {
            if (this.outputLinks.get(i).getId().equals(link.getId())) {
                this.outputLinks.set(i, link);
                link.setSource(this);
                return true;
            }
        }
        return false;
    }
}
