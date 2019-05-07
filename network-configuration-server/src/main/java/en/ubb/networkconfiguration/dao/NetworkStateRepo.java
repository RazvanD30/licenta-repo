package en.ubb.networkconfiguration.dao;

import en.ubb.networkconfiguration.domain.network.runtime.NetworkState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworkStateRepo extends JpaRepository<NetworkState,Long> {
}
