package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.business.validation.exception.NetworkAccessBussExc;
import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineNetwork;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;

import java.util.List;
import java.util.Optional;

public interface OfflineNetworkService {

    OfflineNetwork init(Network network);
    OfflineNetwork reset(OfflineNetwork offlineNetwork);
    OfflineNetwork setInput(OfflineNetwork offlineNetwork, List<Double> inputs) throws NetworkAccessBussExc;
    boolean hasNext(OfflineNetwork network);
    OfflineNetwork next(OfflineNetwork network);
    Optional<OfflineNetwork> findForNetwork(Network network);
    List<OfflineNetwork> getAll();
    Optional<OfflineNetwork> findById(long id);
}
