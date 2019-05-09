package en.ubb.networkconfiguration.service;

import en.ubb.networkconfiguration.boundary.dto.setup.NetworkInitDto;
import en.ubb.networkconfiguration.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.validation.exception.business.DuplicateBussExc;

import java.util.List;
import java.util.Optional;

public interface NetworkInitService {

    List<NetworkInitializer> getAll();

    Optional<NetworkInitializer> findById(long id);

    Optional<NetworkInitializer> findByName(String name);

    NetworkInitializer create(NetworkInitializer networkInitializer) throws DuplicateBussExc;

    boolean deleteById(long id);

}
