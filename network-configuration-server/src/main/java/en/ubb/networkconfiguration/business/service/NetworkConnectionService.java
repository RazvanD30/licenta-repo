package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkConnection;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkLink;

import java.util.List;
import java.util.Optional;

public interface NetworkConnectionService {

    NetworkConnection connect(long sourceId, long destinationId, List<NetworkLink> links) throws NotFoundBussExc;

    List<NetworkConnection> getConnectionsForSource(long sourceId) throws NotFoundBussExc;

    List<NetworkConnection> getConnectionsForDestination(long destinationId) throws NotFoundBussExc;

    NetworkConnection remove(long networkConnectionId) throws NotFoundBussExc;

    Optional<NetworkConnection> findById(long networkConnectionId);

}
