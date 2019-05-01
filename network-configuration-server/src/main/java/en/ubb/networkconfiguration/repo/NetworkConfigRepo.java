package en.ubb.networkconfiguration.repo;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NetworkConfigRepo extends JpaRepository<MultiLayerConfiguration, Long> {

}
