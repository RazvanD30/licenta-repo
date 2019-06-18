package en.ubb.authorization.persistence.dao;

import en.ubb.authorization.persistence.domain.authentication.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthorityRepo extends JpaRepository<Authority, Long>, JpaSpecificationExecutor<Authority> {
}
