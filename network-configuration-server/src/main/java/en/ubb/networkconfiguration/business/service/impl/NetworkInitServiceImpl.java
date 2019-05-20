package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.persistence.dao.NetworkInitRepo;
import en.ubb.networkconfiguration.persistence.dao.specification.NetworkInitSpec;
import en.ubb.networkconfiguration.persistence.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.business.service.NetworkInitService;
import en.ubb.networkconfiguration.business.validation.exception.DuplicateBussExc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NetworkInitServiceImpl implements NetworkInitService {

    private final NetworkInitRepo networkInitRepo;

    @Autowired
    public NetworkInitServiceImpl(NetworkInitRepo networkInitRepo) {
        this.networkInitRepo = networkInitRepo;
    }

    @Override
    public List<NetworkInitializer> getAll() {
        return networkInitRepo.findAll();
    }

    @Override
    public Optional<NetworkInitializer> findById(long id) {
        return networkInitRepo.findById(id);
    }

    @Override
    public Optional<NetworkInitializer> findByName(String name) {
        return networkInitRepo.findOne(Specification.where(NetworkInitSpec.hasName(name)));
    }

    @Override
    public NetworkInitializer create(NetworkInitializer networkInitializer) throws DuplicateBussExc {
        if(this.findByName(networkInitializer.getName()).isPresent()){
            throw new DuplicateBussExc("A network initializer with name " + networkInitializer.getName() + " already exists.");
        }
        return networkInitRepo.save(networkInitializer);
    }

    @Override
    public boolean deleteById(long id) {
        if (networkInitRepo.findById(id).isPresent()) {
            networkInitRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
