package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepo extends JpaRepository<NetworkBranch,Long>, JpaSpecificationExecutor<NetworkBranch> {
}
