package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworkStateRepo extends JpaRepository<NetworkState,Long> {
}
