package en.ubb.networkconfiguration.business.service;


import en.ubb.networkconfiguration.business.nonpersisted.NetworkEval;
import en.ubb.networkconfiguration.business.validation.exception.FileAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NetworkAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.*;
import en.ubb.networkconfiguration.persistence.domain.network.setup.NetworkInitializer;
import sun.nio.ch.Net;

import java.util.List;
import java.util.Optional;

public interface NetworkService {

    List<Network> getAll();

    List<Network> getAllForBranchID(long branchId) throws NotFoundBussExc;

    Optional<Network> findById(long id);

    Optional<Network> findByName(String name);

    Network deleteById(long id) throws NotFoundBussExc;

    Network duplicate(Network source);

    Network create(long branchId, NetworkInitializer initializer) throws NetworkAccessBussExc, NotFoundBussExc;

    Network update(Network updatedNetwork) throws NotFoundBussExc;

    NetworkEval run(Network network, DataFile trainFile, DataFile testFile) throws FileAccessBussExc, NetworkAccessBussExc;

    void saveProgress(Network network) throws NetworkAccessBussExc;

    Network loadNetwork(Network network) throws NetworkAccessBussExc;

    Network addLayer(long networkID, int position, Layer layer);

    Network updateLayer(Layer updatedLayer) throws NetworkAccessBussExc;

    Node updateNode(Node updatedNode) throws NetworkAccessBussExc;

    Link updateLink(Link updatedLink) throws NetworkAccessBussExc;
}
