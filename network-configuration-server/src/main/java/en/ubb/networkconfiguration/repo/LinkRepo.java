package en.ubb.networkconfiguration.repo;

import en.ubb.networkconfiguration.domain.network.runtime.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepo extends JpaRepository<Link, Long> {
}
