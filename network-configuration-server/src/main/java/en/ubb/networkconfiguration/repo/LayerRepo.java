package en.ubb.networkconfiguration.repo;

import en.ubb.networkconfiguration.domain.network.runtime.Layer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LayerRepo extends JpaRepository<Layer, Long> {
}
