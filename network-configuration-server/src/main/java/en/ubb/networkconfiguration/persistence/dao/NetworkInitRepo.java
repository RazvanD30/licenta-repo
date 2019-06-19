package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.setup.NetworkInitializer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkInitRepo extends JpaRepository<NetworkInitializer,Long>, JpaSpecificationExecutor<NetworkInitializer> {
}
