package en.ubb.networkconfiguration.persistence.domain.network.runtime;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineLink;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "links")
public class Link extends BaseEntity<Long> {

    @Column(name = "weight", nullable = false)
    private double weight;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "node_id")
    private Node node;

    @Builder.Default
    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfflineLink> offlineLinks = new ArrayList<>();

    @Builder(toBuilder = true)
    public Link(Long id, double weight, Node node, List<OfflineLink> offlineLinks) {
        super(id);
        this.weight = weight;
        this.node = node;
        this.offlineLinks = offlineLinks;
    }

    public Link(Link link){
        this.weight = link.getWeight();
    }
}
