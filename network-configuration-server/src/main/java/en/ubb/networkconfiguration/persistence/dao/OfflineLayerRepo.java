package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineLayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfflineLayerRepo extends JpaRepository<OfflineLayer, Long> {
}
