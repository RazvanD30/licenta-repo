package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.setup.LayerInitializer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LayerInitRepo extends JpaRepository<LayerInitializer, Long> {

}
