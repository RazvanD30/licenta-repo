package en.ubb.networkconfiguration.persistence.domain.network.offline;

import en.ubb.networkconfiguration.persistence.domain.BaseEntity;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Link;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "offline_links")
public class OfflineLink extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id")
    private Link link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offline_node_id")
    private OfflineNode offlineNode;

    @Builder(toBuilder = true)
    public OfflineLink(Link link, OfflineNode offlineNode) {
        this.link = link;
        this.offlineNode = offlineNode;
    }
}
