package en.ubb.networkconfiguration.service;


import en.ubb.networkconfiguration.domain.network.runtime.Layer;
import en.ubb.networkconfiguration.domain.network.runtime.Network;
import en.ubb.networkconfiguration.domain.network.runtime.Node;
import en.ubb.networkconfiguration.domain.network.setup.NetworkInitializer;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface NetworkService {


    List<Network> getAll();

    Optional<Network> getById(long id);

    boolean deleteById(long id);

    Network create(NetworkInitializer initializer) throws IOException;

    Network update(Network updatedNetwork);

    Network run(Network network) throws IOException, InterruptedException;

    Network saveProgress(Network network) throws IOException;

    Network loadNetwork(Network network) throws IOException;

    Network addLayer(long networkID, int position, Layer layer) throws IOException;

    Network updateLayer(Layer updatedLayer) throws IOException;

    Network updateNode(Node updatedNode) throws IOException;
}
