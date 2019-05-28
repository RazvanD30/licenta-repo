package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineNetwork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfflineNetworkRepo extends JpaRepository<OfflineNetwork, Long> {
}
