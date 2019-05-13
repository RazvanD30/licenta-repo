package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkTrainLog;

import java.util.List;

public interface NetworkTrainLogService {

    List<NetworkTrainLog> getAllSorted(long networkId);

}
