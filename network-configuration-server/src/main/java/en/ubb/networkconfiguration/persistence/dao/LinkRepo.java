package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepo extends JpaRepository<Link, Long> {
}
