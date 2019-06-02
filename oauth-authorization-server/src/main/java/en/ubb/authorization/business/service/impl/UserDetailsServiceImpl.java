package en.ubb.authorization.business.service.impl;

import en.ubb.authorization.persistence.dao.AuthorityRepo;
import en.ubb.authorization.persistence.dao.UserRepo;
import en.ubb.authorization.persistence.dao.specification.AuthoritySpec;
import en.ubb.authorization.persistence.dao.specification.UserSpec;
import en.ubb.authorization.persistence.domain.authentication.User;
import en.ubb.authorization.persistence.domain.authentication.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;



    @Autowired
    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findOne(Specification.where(UserSpec.hasUsername(username)))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("The username %s does not exist", username)));

        return getUserDetails(user);
    }

    public UserDetails loadUserById(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

        return getUserDetails(user);
    }

    private UserDetails getUserDetails(User user) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getAuthorities().forEach(auth -> authorities.add(new SimpleGrantedAuthority(auth.getRole().name())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
