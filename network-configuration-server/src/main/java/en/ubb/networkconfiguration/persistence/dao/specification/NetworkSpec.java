package en.ubb.networkconfiguration.persistence.dao.specification;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.springframework.data.jpa.domain.Specification;

public class NetworkSpec {

    public static Specification<Network> hasName(String name) {
        return (network, cq, cb) -> cb.equal(network.get("name"), name);
    }
}
