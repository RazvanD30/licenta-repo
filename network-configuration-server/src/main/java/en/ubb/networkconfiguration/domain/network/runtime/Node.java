package en.ubb.networkconfiguration.domain.network.runtime;


import en.ubb.networkconfiguration.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nodes")
public class Node extends BaseEntity<Long> {

    @Column(name = "bias")
    private double bias;

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL)
    private List<Link> outputLinks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "layer_id")
    private Layer layer;

    @Builder(toBuilder = true)
    public Node(Long id, double bias, List<Link> outputLinks, Layer layer) {
        super(id);
        this.bias = bias;
        this.outputLinks = outputLinks;
        this.layer = layer;
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
