package en.ubb.networkconfiguration.persistence.dao.specification;

import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualNetwork;
import org.springframework.data.jpa.domain.Specification;

public class VirtualNetworkSpec {

    public static Specification<VirtualNetwork> hasName(String name) {
        return (virtualNetwork, cq, cb) -> cb.equal(virtualNetwork.get("name"), name);
    }

}
