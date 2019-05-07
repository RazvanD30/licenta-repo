package en.ubb.networkconfiguration.domain.network.runtime;

import en.ubb.networkconfiguration.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
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

    @Builder(toBuilder = true)
    public Link(Long id, double weight, Node node) {
        super(id);
        this.weight = weight;
        this.node = node;
    }
}
