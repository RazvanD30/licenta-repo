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
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "network_states")
public class NetworkState extends BaseEntity<Long> {

    @Lob
    @Column(name = "descriptor")
    private byte[] descriptor;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private List<Network> networks = new ArrayList<>();

    @Builder(toBuilder = true)
    public NetworkState(Long id, byte[] descriptor, List<Network> networks) {
        super(id);
        this.descriptor = descriptor;
        this.networks = networks;
    }

    public void addNetwork(Network network) {
        this.networks.add(network);
        network.setState(this);
    }

    public boolean removeNetwork(Network network) {
        boolean removed = this.networks.remove(network);
        if (removed) {
            network.setState(null);
        }
        return removed;
    }

    public boolean updateNetwork(Network network) {
        for (int i = 0; i < this.networks.size(); i++) {
            if (this.networks.get(i).getId().equals(network.getId())) {
                this.networks.set(i, network);
                network.setState(this);
                return true;
            }
        }
        return false;
    }

}
