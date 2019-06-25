package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.Layer;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class NetworkRepo extends SimpleJpaRepository<Network, Long> implements JpaSpecificationExecutor<Network> {

    private final LayerRepo layerRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public NetworkRepo(EntityManager em, LayerRepo layerRepo) {
        super(Network.class, em);
        this.entityManager = em;
        this.layerRepo = layerRepo;
    }

    public Network batchSave(Network network) {
        List<Long> layerIDs = layerRepo.bulkSave(network.getLayers().stream()
                .peek(layer -> layer.setNetwork(null))
                .collect(Collectors.toList())).stream()
                .map(Layer::getId)
                .collect(Collectors.toList());
        network.setLayers(new ArrayList<>());
        Network persistedNetwork = super.save(network);
        layerIDs.forEach(layerID -> {
            persistedNetwork.addLayer(layerRepo.getOne(layerID));
            super.save(persistedNetwork);
        });
        entityManager.flush();
        entityManager.clear();
        return persistedNetwork;
    }
}
