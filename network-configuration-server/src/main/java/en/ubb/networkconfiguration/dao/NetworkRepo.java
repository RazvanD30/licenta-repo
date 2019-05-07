package en.ubb.networkconfiguration.dao;

import en.ubb.networkconfiguration.domain.network.runtime.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NetworkRepo extends JpaRepository<Network, Long> {

}
