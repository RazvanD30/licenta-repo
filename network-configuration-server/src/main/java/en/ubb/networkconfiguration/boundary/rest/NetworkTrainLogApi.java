package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.network.log.NetworkTrainLogDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.business.service.NetworkTrainLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("network-management/log")
public class NetworkTrainLogApi {

    private final NetworkTrainLogService networkTrainLogService;

    @Autowired
    public NetworkTrainLogApi(NetworkTrainLogService networkTrainLogService) {
        this.networkTrainLogService = networkTrainLogService;
    }

    @GetMapping(value = "/{networkId}")
    public List<NetworkTrainLogDto> getAllSortedByDate(@NotNull @PathVariable Long networkId) {
        return networkTrainLogService.getAllSorted(networkId).stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
