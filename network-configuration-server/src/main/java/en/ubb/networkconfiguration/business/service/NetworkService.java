package en.ubb.networkconfiguration.business.service;


import en.ubb.networkconfiguration.persistence.domain.network.runtime.*;
import en.ubb.networkconfiguration.persistence.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.business.validation.exception.FileAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NetworkAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;

import java.util.List;
import java.util.Optional;

public interface NetworkService {

    List<Network> getAll();

    Optional<Network> findById(long id);

    boolean deleteById(long id);

    Network create(NetworkInitializer initializer) throws NetworkAccessBussExc;

    Network update(Network updatedNetwork) throws NotFoundBussExc;

    Network run(Network network, DataFile trainFile, DataFile testFile) throws FileAccessBussExc;

    Network saveProgress(Network network) throws NetworkAccessBussExc;

    Network loadNetwork(Network network) throws NetworkAccessBussExc;

    Network addLayer(long networkID, int position, Layer layer);

    Network updateLayer(Layer updatedLayer) throws NetworkAccessBussExc;

    Node updateNode(Node updatedNode) throws NetworkAccessBussExc;

    Link updateLink(Link updatedLink) throws NetworkAccessBussExc;
}
