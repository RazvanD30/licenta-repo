package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.business.service.NetworkTrainLogService;
import en.ubb.networkconfiguration.persistence.dao.NetworkTrainLogRepo;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkTrainLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NetworkTrainLogServiceImpl implements NetworkTrainLogService {

    private final NetworkTrainLogRepo networkTrainLogRepo;

    @Autowired
    public NetworkTrainLogServiceImpl(NetworkTrainLogRepo networkTrainLogRepo) {
        this.networkTrainLogRepo = networkTrainLogRepo;
    }


    @Override
    public List<NetworkTrainLog> getAllSorted(long networkId) {
        return networkTrainLogRepo.getAllOrderByDateAsc(networkId);
    }
}

