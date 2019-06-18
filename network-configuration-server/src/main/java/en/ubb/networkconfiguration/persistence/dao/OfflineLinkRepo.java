package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfflineLinkRepo extends JpaRepository<OfflineLink, Long> {
}
