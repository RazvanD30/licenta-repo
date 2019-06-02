package en.ubb.networkconfiguration.persistence.domain.authentication;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.authentication.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority extends BaseEntity<Long> {

    @Column(name = "role", nullable = false, length = 50, unique = true)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "authorities")
    private List<User> users = new ArrayList<>();

    @Builder(toBuilder = true)
    public Authority(Role role, List<User> users) {
        this.role = role;
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
        user.getAuthorities().add(this);
    }

    public boolean removeUser(User user) {
        boolean removed = this.users.remove(user);
        if (removed) {
            user.getAuthorities().remove(this);
        }
        return removed;
    }
}
