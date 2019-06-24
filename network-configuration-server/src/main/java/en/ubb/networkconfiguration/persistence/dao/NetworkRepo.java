package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Node;
import en.ubb.networkconfiguration.persistence.domain.network.setup.NetworkInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface NetworkRepo extends JpaRepository<Network, Long>, JpaSpecificationExecutor<Network> {

}
