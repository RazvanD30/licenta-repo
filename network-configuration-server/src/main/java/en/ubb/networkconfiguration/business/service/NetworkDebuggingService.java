package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.boundary.dto.network.debugging.DebuggingNode;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;

import java.util.List;

public interface NetworkDebuggingService {

    List<DebuggingNode> initialize(Network network);

    void initializeData(DebuggingNode node);

    List<DebuggingNode> next(DebuggingNode node);
}
