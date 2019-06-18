package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.network.offline.OfflineNetworkDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.business.service.OfflineNetworkService;
import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("network-management")
public class OfflineNetworkApi  {


    private final OfflineNetworkService offlineNetworkService;

    @Autowired
    public OfflineNetworkApi(OfflineNetworkService offlineNetworkService) {
        this.offlineNetworkService = offlineNetworkService;
    }

    @GetMapping("offline/{id}")
    public OfflineNetworkDto getById(@PathVariable Long id){
        //TODO
        return DtoMapper.toDto(offlineNetworkService.getAll().get(0));
    }

    @PostMapping("offline")
    public OfflineNetworkDto next(@RequestBody OfflineNetworkDto offlineNetworkDto){



       // if(offlineNetworkService.hasNext(DtoMapper.fromDto(offlineNetworkDto)))
        return null;
    }

}
