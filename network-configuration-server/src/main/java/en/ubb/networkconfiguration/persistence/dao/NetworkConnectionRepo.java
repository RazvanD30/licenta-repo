package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkConnectionRepo extends JpaRepository<NetworkConnection, Long>, JpaSpecificationExecutor<NetworkConnection> {
}
