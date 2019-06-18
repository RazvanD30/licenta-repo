package en.ubb.authorization.business.service;

import en.ubb.authorization.persistence.domain.authentication.User;
import en.ubb.authorization.persistence.domain.authentication.enums.Role;

import java.util.List;

public interface UserService {
    User register(String username, String password, List<Role> roles);
}
