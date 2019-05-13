package en.ubb.networkconfiguration.persistence.domain.network.runtime;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "network_links",
        uniqueConstraints = @UniqueConstraint(columnNames = {"source_id", "destination_id"}))
public class NetworkLink extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_id")
    private Node source;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_id")
    private Node destination;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "network_connection_id")
    private NetworkConnection networkConnection;

    @Builder(toBuilder = true)
    public NetworkLink(Long id, Node source, Node destination, NetworkConnection networkConnection) {
        super(id);
        this.source = source;
        this.destination = destination;
        this.networkConnection = networkConnection;
    }
}
