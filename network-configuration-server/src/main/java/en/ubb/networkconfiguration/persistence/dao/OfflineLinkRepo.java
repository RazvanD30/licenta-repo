package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfflineLinkRepo extends JpaRepository<OfflineLink, Long> {
}
