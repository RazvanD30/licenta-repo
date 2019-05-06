package en.ubb.networkconfiguration.domain.network.runtime;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class NetworkFileId {

    @Column(name = "network_id")
    private Long networkId;

    @Column(name = "file_id")
    private Long fileId;

}
