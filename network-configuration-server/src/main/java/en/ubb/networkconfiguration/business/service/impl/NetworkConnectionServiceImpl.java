package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.business.service.NetworkConnectionService;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.dao.NetworkConnectionRepo;
import en.ubb.networkconfiguration.persistence.dao.NetworkRepo;
import en.ubb.networkconfiguration.persistence.dao.specification.NetworkConnectionSpec;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkConnection;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NetworkConnectionServiceImpl implements NetworkConnectionService {


    private final NetworkConnectionRepo networkConnectionRepo;

    private final NetworkRepo networkRepo;

    @Autowired
    public NetworkConnectionServiceImpl(NetworkConnectionRepo networkConnectionRepo, NetworkRepo networkRepo) {
        this.networkConnectionRepo = networkConnectionRepo;
        this.networkRepo = networkRepo;
    }

    @Override
    public NetworkConnection connect(long sourceId, long destinationId, List<NetworkLink> links) throws NotFoundBussExc {

        Network source = networkRepo.findById(sourceId)
                .orElseThrow(() -> new NotFoundBussExc("Source network with id " + sourceId + " not found"));
        Network destination = networkRepo.findById(destinationId)
                .orElseThrow(() -> new NotFoundBussExc("Destination network with id " + destinationId + " not found"));

        NetworkConnection networkConnection = NetworkConnection.builder()
                .source(source)
                .destination(destination)
                .build();
        links.forEach(networkConnection::addLink);
        this.networkConnectionRepo.save(networkConnection);

        return null;
    }

    @Override
    public List<NetworkConnection> getConnectionsForSource(long sourceId) throws NotFoundBussExc {

        Network source = networkRepo.findById(sourceId)
                .orElseThrow(() -> new NotFoundBussExc("Source network with id " + sourceId + " not found"));

        return this.networkConnectionRepo.findAll(NetworkConnectionSpec.hasSource(source));
    }

    @Override
    public List<NetworkConnection> getConnectionsForDestination(long destinationId) throws NotFoundBussExc {

        Network destination = networkRepo.findById(destinationId)
                .orElseThrow(() -> new NotFoundBussExc("Destination network with id " + destinationId + " not found"));

        return this.networkConnectionRepo.findAll(NetworkConnectionSpec.hasDestination(destination));
    }

    @Override
    public NetworkConnection remove(long networkConnectionId) throws NotFoundBussExc {

        NetworkConnection networkConnection = this.networkConnectionRepo.findById(networkConnectionId)
                .orElseThrow(() -> new NotFoundBussExc("Network connection with id " + networkConnectionId + " not found"));

        this.networkConnectionRepo.delete(networkConnection);
        return networkConnection;
    }

    @Override
    public Optional<NetworkConnection> findById(long networkConnectionId) {
        return this.networkConnectionRepo.findById(networkConnectionId);
    }
}
