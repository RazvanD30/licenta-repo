package en.ubb.networkconfiguration.persistence.dao.specification;

import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.network.NetworkBranch;
import org.springframework.data.jpa.domain.Specification;

public class BranchSpec {

    public static Specification<NetworkBranch> hasOwner(User user) {
        return (branch, cq, cb) -> cb.equal(branch.get("owner"), user);
    }

    public static Specification<NetworkBranch> hasContributor(User user) {
        return (branch, cq, cb) -> cb.isMember(user, branch.get("contributors"));
    }
}
