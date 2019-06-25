package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Layer;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LayerRepo extends SimpleJpaRepository<Layer, Long> implements JpaSpecificationExecutor<Layer> {

    @PersistenceContext
    private EntityManager entityManager;

    private final NodeRepo nodeRepo;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Autowired
    public LayerRepo(EntityManager em, NodeRepo nodeRepo) {
        super(Layer.class, em);
        this.entityManager = em;
        this.nodeRepo = nodeRepo;
    }

    @Transactional
    public <T extends Layer> List<Layer> bulkSave(List<Layer> entities) {
        return null;
    }
}