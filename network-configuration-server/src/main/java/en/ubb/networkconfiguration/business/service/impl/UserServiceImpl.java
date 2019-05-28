package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.business.service.UserService;
import en.ubb.networkconfiguration.business.validation.exception.DuplicateBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.dao.AuthorityRepo;
import en.ubb.networkconfiguration.persistence.dao.UserRepo;
import en.ubb.networkconfiguration.persistence.dao.specification.AuthoritySpec;
import en.ubb.networkconfiguration.persistence.dao.specification.UserSpec;
import en.ubb.networkconfiguration.persistence.domain.authentication.Authority;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.authentication.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final AuthorityRepo authorityRepo;

    private final BCryptPasswordEncoder encoder;

    private User currentUser;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, AuthorityRepo authorityRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.encoder = encoder;
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findOne(Specification.where(UserSpec.hasUsername(username)));
    }

    @Override
    public boolean credentialsMatch(String username, String password) throws NotFoundBussExc {
        User user = this.findByUsername(username)
                .orElseThrow(() -> new NotFoundBussExc("User with username " + username + " not found"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(user.getPassword(), password);
    }

    @Override
    public User create(String username, String password, Role role) throws DuplicateBussExc {

        if (findByUsername(username).isPresent()) {
            throw new DuplicateBussExc("Username " + username + " has been already taken.");
        }


        Authority authority = this.authorityRepo.findOne(Specification.where(AuthoritySpec.hasRole(role)))
                .orElseGet(() -> {
                    Authority newAuthority = Authority.builder()
                            .role(role)
                            .users(new HashSet<>())
                            .build();
                    return this.authorityRepo.save(newAuthority); //TODO authority service
                });
        User user = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .enabled(true)
                .build();
        authority.addUser(user);
        return user;
    }

    @Override
    public User update(User user) throws NotFoundBussExc {
        deleteById(user.getId());
        return userRepo.save(user);
    }

    @Override
    public User deleteById(long id) throws NotFoundBussExc {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundBussExc("User with id " + id + " not found"));
        userRepo.deleteById(id);
        return user;
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @Override
    public User getCurrentUser() {
        return this.currentUser;
    }

    @Override
    public void removeCurrentUser() {
        this.currentUser = null;
    }
}
