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

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<NetworkBranch> branchesWithOwnership = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @ManyToMany
    private List<NetworkBranch> branches = new ArrayList<>();

    @Builder(toBuilder = true)
    public User(String username, String password, boolean enabled, List<NetworkBranch> branchesWithOwnership,
                Authority authority, List<NetworkBranch> branches) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.branchesWithOwnership = branchesWithOwnership;
        this.authority = authority;
        this.branches = branches;
    }
}
