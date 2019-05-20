package en.ubb.networkconfiguration.persistence.dao.specification;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkConnection;
import org.springframework.data.jpa.domain.Specification;

public class NetworkConnectionSpec {

    public static Specification<NetworkConnection> hasSource(Network source) {
        return (networkConnection, cq, cb) -> cb.equal(networkConnection.get("source"), source);
    }

    public static Specification<NetworkConnection> hasDestination(Network destination) {
        return (networkConnection, cq, cb) -> cb.equal(networkConnection.get("destination"), destination);
    }
}
