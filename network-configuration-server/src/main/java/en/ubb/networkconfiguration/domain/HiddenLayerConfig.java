package en.ubb.networkconfiguration.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.deeplearning4j.nn.conf.layers.FeedForwardLayer;
import org.deeplearning4j.nn.conf.layers.Layer;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "hidden_layer_configs")
public class HiddenLayerConfig extends BaseEntity<Long> {

    @Column(name = "hidden_nodes")
    private int hiddenNodes;

    @ManyToOne
    private NetworkConfig networkConfig;

    private FeedForwardLayer.Builder builder;

    private int inputsCount;
}
