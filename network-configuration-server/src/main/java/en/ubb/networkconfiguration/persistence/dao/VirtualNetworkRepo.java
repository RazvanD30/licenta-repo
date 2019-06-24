package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualNetworkRepo extends JpaRepository<VirtualNetwork, Long>, JpaSpecificationExecutor<VirtualNetwork> {
}
