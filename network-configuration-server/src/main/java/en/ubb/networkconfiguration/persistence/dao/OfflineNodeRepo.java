package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfflineNodeRepo extends JpaRepository<OfflineNode, Long> {
}
