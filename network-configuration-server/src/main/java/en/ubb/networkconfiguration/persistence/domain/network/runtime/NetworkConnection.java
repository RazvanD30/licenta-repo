package en.ubb.networkconfiguration.persistence.domain.network.runtime;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "network_connections",
        uniqueConstraints = @UniqueConstraint(columnNames = {"source_id", "destination_id"}))
public class NetworkConnection extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_id")
    private Network source;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_id")
    private Network destination;

    @Builder.Default
    @OneToMany(mappedBy = "networkConnection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NetworkLink> networkLinks = new ArrayList<>();

    @Builder(toBuilder = true)
    public NetworkConnection(Long id, Network source, Network destination, List<NetworkLink> networkLinks) {
        super(id);
        this.source = source;
        this.destination = destination;
        this.networkLinks = networkLinks;
    }

    public void addLink(NetworkLink networkLink){
        this.networkLinks.add(networkLink);
        networkLink.setNetworkConnection(this);
    }

    public boolean removeLink(NetworkLink networkLink){
        boolean removed = this.networkLinks.remove(networkLink);
        if (removed) {
            networkLink.setNetworkConnection(null);
        }
        return removed;
    }
}
