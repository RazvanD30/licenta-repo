package en.ubb.statistics.persistence.models;

import en.ubb.statistics.persistence.enums.OperationType;

import javax.persistence.*;

@Entity
@Table(name = "operations")
public class Operation extends BaseEntity<Long>{

    @Column(name = "type")
    private OperationType type;

    @Column(name = "modified_property")
    private String modifiedProperty;

    @Column(name = "previous_value")
    private int previousValue;

    @Column(name = "current_value")
    private int currentValue;

    //TODO add Node type to this
    @Column(name = "source_node")
    private int sourceNode;

    //TODO add Node type to this
    @Column(name = "target_node")
    private int targetNode;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private CompoundOperation compoundOperation;



}
