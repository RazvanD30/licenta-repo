package en.ubb.networkconfiguration.persistence.domain.authentication;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.authentication.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority extends BaseEntity<Long> {

    @Column(name = "role", nullable = false, length = 50, unique = true)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

    @Builder(toBuilder = true)
    public Authority(Role role, Set<User> users) {
        this.role = role;
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
        user.setAuthority(this);
    }

    public boolean removeUser(User user) {
        boolean removed = this.users.contains(user);
        if (removed) {
            this.users.remove(user);
        }
        return removed;
    }
}
