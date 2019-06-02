package en.ubb.networkconfiguration.persistence.domain.authentication;


import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.NetworkBranch;
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
@Table(name = "users")
public class User extends BaseEntity<Long> {

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @OneToMany(mappedBy = "owner")
    private List<NetworkBranch> branchesWithOwnership = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<Authority> authorities = new ArrayList<>();

    @ManyToMany(mappedBy = "contributors")
    private List<NetworkBranch> branches = new ArrayList<>();

    @Builder(toBuilder = true)
    public User(Long id, String username, String password, boolean enabled, List<NetworkBranch> branchesWithOwnership, List<Authority> authorities, List<NetworkBranch> branches) {
        super(id);
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.branchesWithOwnership = branchesWithOwnership;
        this.authorities = authorities;
        this.branches = branches;
    }

    public void addAuthority(Authority authority){
        this.authorities.add(authority);
        authority.getUsers().add(this);
    }
}
