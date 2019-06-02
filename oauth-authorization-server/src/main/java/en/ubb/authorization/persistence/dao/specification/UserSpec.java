package en.ubb.authorization.persistence.dao.specification;


import en.ubb.authorization.persistence.domain.authentication.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpec {

    public static Specification<User> hasUsername(String username) {
        return (user, cq, cb) -> cb.equal(user.get("username"), username);
    }
}
