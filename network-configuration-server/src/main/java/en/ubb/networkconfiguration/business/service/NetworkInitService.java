package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.persistence.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.business.validation.exception.DuplicateBussExc;

import java.util.List;
import java.util.Optional;

public interface NetworkInitService {

    List<NetworkInitializer> getAll();

    Optional<NetworkInitializer> findById(long id);

    Optional<NetworkInitializer> findByName(String name);

    NetworkInitializer create(NetworkInitializer networkInitializer) throws DuplicateBussExc;

    boolean deleteById(long id);

}
