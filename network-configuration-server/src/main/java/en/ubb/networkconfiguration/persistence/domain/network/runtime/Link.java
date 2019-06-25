package en.ubb.networkconfiguration.persistence.domain.network.runtime;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualLink;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "outputLinks")
public class Link extends BaseEntity<Long> {

    @Column(name = "weight", nullable = false)
    private double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Node source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private Node destination;

    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VirtualLink> virtualLinks = new ArrayList<>();

    @Builder(toBuilder = true)
    public Link(Long id, double weight, Node source, Node destination, List<VirtualLink> virtualLinks) {
        super(id);
        this.weight = weight;
        this.source = source;
        this.destination = destination;
        this.virtualLinks = virtualLinks;
    }

    public Link(Link link) {
        this.weight = link.getWeight();
    }
}
