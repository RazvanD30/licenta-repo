package en.ubb.networkconfiguration.dao;

import en.ubb.networkconfiguration.domain.network.setup.NetworkInitializer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NetworkInitRepo extends JpaRepository<NetworkInitializer,Long>, JpaSpecificationExecutor<NetworkInitializer> {
}
