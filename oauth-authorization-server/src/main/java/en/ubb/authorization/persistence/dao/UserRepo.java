package en.ubb.authorization.persistence.dao;

import en.ubb.authorization.persistence.domain.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepo extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
}
