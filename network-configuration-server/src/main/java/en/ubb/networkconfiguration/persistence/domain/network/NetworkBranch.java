package en.ubb.networkconfiguration.persistence.domain.network;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

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
    public NetworkBranch(String name, BranchType type, List<Network> networks, User owner, List<User> contributors) {
        this.name = name;
        this.type = type;
        this.networks = networks;
        this.owner = owner;
        this.contributors = contributors;
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
}
