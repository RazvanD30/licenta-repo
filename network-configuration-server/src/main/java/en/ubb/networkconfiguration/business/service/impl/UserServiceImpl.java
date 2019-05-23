package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.business.service.UserService;
import en.ubb.networkconfiguration.business.util.EncryptionUtil;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.dao.UserRepo;
import en.ubb.networkconfiguration.persistence.dao.specification.UserSpec;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private User currentUser;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
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

        EncryptionUtil encryptionUtil = new EncryptionUtil();
        return encryptionUtil.checkPassword(user.getPassword().toCharArray(), password);
    }

    @Override
    public User create(User user) {
        return userRepo.save(user);
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
