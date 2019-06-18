package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.NetworkBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BranchRepo extends JpaRepository<NetworkBranch,Long>, JpaSpecificationExecutor<NetworkBranch> {
}
