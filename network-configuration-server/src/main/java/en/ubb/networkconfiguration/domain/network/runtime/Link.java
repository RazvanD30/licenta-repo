package en.ubb.networkconfiguration.domain.network.runtime;

import en.ubb.networkconfiguration.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "links")
public class Link extends BaseEntity<Long> {

    @Column(name = "weight")
    private double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private Node node;

    @Builder(toBuilder = true)
    public Link(Long id, double weight, Node node) {
        super(id);
        this.weight = weight;
        this.node = node;
    }
}
