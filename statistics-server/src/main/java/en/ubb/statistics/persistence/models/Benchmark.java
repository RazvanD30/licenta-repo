package en.ubb.statistics.persistence.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "benchmarks")
public class Benchmark extends BaseEntity<Long>{


    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "efficiency_improvement")
    private int efficiencyImprovement;

    @Column(name = "confidence_improvement")
    private int confidenceImprovement;

    @Column(name = "batch_size")
    private int batchSize;

    @Column(name = "test_passing_percentage")
    private int testPassingPercentage;

    @ManyToMany(mappedBy = "relatedBenchmarks")
    private List<CompoundOperation> compoundOperations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "compared_to", referencedColumnName = "id")
    private Benchmark comparedTo;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getEfficiencyImprovement() {
        return efficiencyImprovement;
    }

    public void setEfficiencyImprovement(int efficiencyImprovement) {
        this.efficiencyImprovement = efficiencyImprovement;
    }

    public int getConfidenceImprovement() {
        return confidenceImprovement;
    }

    public void setConfidenceImprovement(int confidenceImprovemnt) {
        this.confidenceImprovement = confidenceImprovemnt;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getTestPassingPercentage() {
        return testPassingPercentage;
    }

    public void setTestPassingPercentage(int testPassingPercentage) {
        this.testPassingPercentage = testPassingPercentage;
    }

    public Benchmark getComparedTo() {
        return comparedTo;
    }

    public void setComparedTo(Benchmark comparedTo) {
        this.comparedTo = comparedTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Benchmark benchmark = (Benchmark) o;
        return efficiencyImprovement == benchmark.efficiencyImprovement &&
                confidenceImprovement == benchmark.confidenceImprovement &&
                batchSize == benchmark.batchSize &&
                testPassingPercentage == benchmark.testPassingPercentage &&
                Objects.equals(createdAt, benchmark.createdAt) &&
                Objects.equals(comparedTo, benchmark.comparedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, efficiencyImprovement, confidenceImprovement, batchSize, testPassingPercentage, comparedTo);
    }

    public List<CompoundOperation> getCompoundOperations() {
        return compoundOperations;
    }

    public void setCompoundOperations(List<CompoundOperation> compoundOperations) {
        this.compoundOperations = compoundOperations;
    }
}
