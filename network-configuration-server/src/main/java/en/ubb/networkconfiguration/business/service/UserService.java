package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();
    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    boolean credentialsMatch(String username, String password) throws NotFoundBussExc;
    User create(User user);
    User update(User user) throws NotFoundBussExc;
    User deleteById(long id) throws NotFoundBussExc;
    void setCurrentUser(User user);
    User getCurrentUser();
    void removeCurrentUser();
}
