package en.ubb.networkconfiguration.persistence.dao.specification;

import en.ubb.networkconfiguration.persistence.domain.authentication.Authority;
import en.ubb.networkconfiguration.persistence.domain.authentication.enums.Role;
import org.springframework.data.jpa.domain.Specification;

public class AuthoritySpec {

    public static Specification<Authority> hasRole(Role role) {
        return (authority, cq, cb) -> cb.equal(authority.get("role"), role);
    }
}
