package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Link;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LinkRepo extends SimpleJpaRepository<Link, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    public LinkRepo(EntityManager em) {
        super(Link.class, em);
        this.entityManager = em;
    }

    @Transactional
    public <T extends Link> List<Link> bulkSave(List<Link> entities) {
        final List<Link> savedEntities = new ArrayList<>(entities.size());
        int i = 0;
        for (Link t : entities) {
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
