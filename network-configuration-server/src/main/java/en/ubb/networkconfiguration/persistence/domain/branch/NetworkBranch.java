package en.ubb.networkconfiguration.persistence.domain.branch;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.network.enums.BranchType;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "branches")
public class NetworkBranch extends BaseEntity<Long> {

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "type", length = 50)
    @Enumerated(EnumType.STRING)
    private BranchType type;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Network> networks = new ArrayList<>();

    @Column(name = "source_id")
    private Long sourceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "currentBranch", cascade = CascadeType.ALL)
    private List<User> currentUsers = new ArrayList<>();

    @Column(name = "created_datetime")
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Column(name = "updated_datetime")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToMany
    @JoinTable(
            name = "branches_users",
            joinColumns = @JoinColumn(name = "branch_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> contributors = new ArrayList<>();

    @Builder(toBuilder = true)
    public NetworkBranch(Long id, String name, BranchType type, List<Network> networks, Long sourceId, User owner,
                         LocalDateTime createDateTime, LocalDateTime updateDateTime, List<User> contributors,
                         List<User> currentUsers) {
        super(id);
        this.name = name;
        this.type = type;
        this.networks = networks;
        this.sourceId = sourceId;
        this.owner = owner;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
        this.contributors = contributors;
        this.currentUsers = currentUsers;
    }



    public void addNetwork(Network network){
        this.networks.add(network);
        network.setBranch(this);
    }

    public boolean removeNetwork(Network network){
        boolean removed = this.networks.remove(network);
        if(removed){
            network.setBranch(null);
        }
        return removed;
    }

    public void addContributor(User user){
        this.contributors.add(user);
        user.getBranches().add(this);
    }

    public void removeContributor(User user){
        this.contributors.removeIf(u -> u.getId().equals(user.getId()));
        user.getBranches().removeIf(b -> b.getId().equals(this.getId()));
    }

    public void addCurrentUser(User user){
        this.currentUsers.add(user);
        user.setCurrentBranch(this);
    }
}
