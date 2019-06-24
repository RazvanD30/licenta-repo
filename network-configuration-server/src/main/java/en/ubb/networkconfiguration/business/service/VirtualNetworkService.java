package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.business.validation.exception.NetworkAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualLayer;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualNetwork;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;

import java.util.List;
import java.util.Optional;

public interface VirtualNetworkService {

    VirtualNetwork init(Network network, String name);

    List<VirtualNetwork> getForNetworkId(long networkId) throws NotFoundBussExc;

    Optional<VirtualNetwork> getByName(String name);

    VirtualLayer getForIdAtPos(long id, int pos) throws NotFoundBussExc;

    VirtualNetwork reset(VirtualNetwork virtualNetwork);
    VirtualNetwork setInput(VirtualNetwork virtualNetwork, List<Double> inputs) throws NetworkAccessBussExc;
    boolean hasNext(VirtualNetwork network);
    VirtualNetwork next(VirtualNetwork network);
    Optional<VirtualNetwork> findForNetwork(Network network);
    List<VirtualNetwork> getAll();
    Optional<VirtualNetwork> findById(long id);
}
