package en.ubb.statistics.persistence.models;


import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;


// update(offlineNode) set w1 = 2, w3 = 4
@Entity
@Table(name = "operations")
public class CompoundOperation extends BaseEntity<Long> {


    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "operation_&_benchmark",
            joinColumns = @JoinColumn(name = "operation_id"),
            inverseJoinColumns = @JoinColumn(name = "benchmark_id"))
    private Set<Benchmark> relatedBenchmarks = new HashSet<>();


    @OneToMany(mappedBy = "compoundOperation")
    private Set<Operation> operations = new HashSet<>();

    //TODO Make this FK to User
    @Column(name = "user")
    private int user;




}
