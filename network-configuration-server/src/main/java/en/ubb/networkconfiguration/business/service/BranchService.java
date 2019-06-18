package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface BranchService {

    List<NetworkBranch> getAll();

    List<NetworkBranch> getAllForOwner(String username) throws NotFoundBussExc;

    List<NetworkBranch> getAllForContributor(String username) throws NotFoundBussExc;

    List<NetworkBranch> getAllForUser(String username) throws NotFoundBussExc;

    Optional<NetworkBranch> findById(long id);

    Optional<NetworkBranch> findByName(String name);

    NetworkBranch create(NetworkBranch newBranch, NetworkBranch origin) throws NotFoundBussExc;


    NetworkBranch pull(long toId, long fromId) throws NotFoundBussExc;

    NetworkBranch update(long id, NetworkBranch newBranch) throws NotFoundBussExc;

    NetworkBranch delete(long id) throws NotFoundBussExc;

}
