package en.ubb.networkconfiguration.dao.specification;

import en.ubb.networkconfiguration.domain.network.setup.NetworkInitializer;
import org.springframework.data.jpa.domain.Specification;

public class NetworkInitSpec {

    public static Specification<NetworkInitializer> hasName(String name) {
        return (networkInitializer, cq, cb) -> cb.equal(networkInitializer.get("name"), name);
    }
}
