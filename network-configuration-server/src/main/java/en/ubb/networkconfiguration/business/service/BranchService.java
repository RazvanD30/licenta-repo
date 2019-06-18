package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.network.NetworkBranch;

import java.util.List;
import java.util.Optional;

public interface BranchService {

    NetworkBranch create(NetworkBranch newBranch);

    NetworkBranch create(NetworkBranch newBranch, NetworkBranch origin);

    NetworkBranch pull(NetworkBranch to, NetworkBranch from);

    boolean remove(NetworkBranch branch);

    List<NetworkBranch> getAll();

    List<NetworkBranch> getAllForOwner(User owner);

    List<NetworkBranch> getAllForUser(User user);

    Optional<NetworkBranch> findById(Long id);
}
