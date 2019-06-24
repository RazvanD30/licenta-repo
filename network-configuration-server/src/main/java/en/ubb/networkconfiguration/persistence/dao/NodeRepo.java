package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Node;
import en.ubb.networkconfiguration.persistence.domain.network.setup.NetworkInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NodeRepo extends SimpleJpaRepository<Node, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    public NodeRepo(EntityManager em) {
        super(Node.class, em);
        this.entityManager = em;
    }

    @Transactional
    public <T extends Node> List<Node> bulkSave(List<Node> entities) {
        final List<Node> savedEntities = new ArrayList<>(entities.size());
        int i = 0;
        for (Node t : entities) {
            savedEntities.add(save(t));
            i++;
            if (i % batchSize == 0) {
                // Flush a batch of inserts and release memory.
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
        return savedEntities;
    }

}
