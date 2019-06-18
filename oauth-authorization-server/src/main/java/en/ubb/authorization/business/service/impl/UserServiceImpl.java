package en.ubb.authorization.business.service.impl;

import en.ubb.authorization.persistence.dao.AuthorityRepo;
import en.ubb.authorization.persistence.dao.UserRepo;
import en.ubb.authorization.persistence.dao.specification.AuthoritySpec;
import en.ubb.authorization.persistence.dao.specification.UserSpec;
import en.ubb.authorization.persistence.domain.authentication.User;
import en.ubb.authorization.persistence.domain.authentication.enums.Role;
import en.ubb.authorization.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final AuthorityRepo authorityRepo;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, AuthorityRepo authorityRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.encoder = encoder;
    }

    @Override
    public User register(String username, String password, List<Role> roles) {
        if (userRepo.findOne(Specification.where(UserSpec.hasUsername(username))).isPresent()) {
            throw new RuntimeException("Username " + username + " has been already taken.");
        }

        User user = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .authorities(new ArrayList<>())
                .enabled(true)
                .build();

        roles.forEach(role ->
                this.authorityRepo.findOne(Specification.where(AuthoritySpec.hasRole(role)))
                        .ifPresent(user::addAuthority)
        );

       return this.userRepo.save(user);
    }
}
