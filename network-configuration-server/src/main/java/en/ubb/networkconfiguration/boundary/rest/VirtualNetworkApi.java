package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.network.virtual.VirtualLayerDto;
import en.ubb.networkconfiguration.boundary.dto.network.virtual.VirtualNetworkDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.boundary.validation.exception.NotFoundException;
import en.ubb.networkconfiguration.business.service.NetworkService;
import en.ubb.networkconfiguration.business.service.VirtualNetworkService;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("network-management/virtual-simulation")
public class VirtualNetworkApi {


    private final VirtualNetworkService virtualNetworkService;

    private final NetworkService networkService;

    @Autowired
    public VirtualNetworkApi(VirtualNetworkService virtualNetworkService, NetworkService networkService) {
        this.virtualNetworkService = virtualNetworkService;
        this.networkService = networkService;
    }


    @PostMapping
    public VirtualNetworkDto create(@RequestBody VirtualNetworkDto dto) throws NotFoundException {
        Network network;
        if(dto.getNetworkId() != null) {
            network = this.networkService.findById(dto.getNetworkId())
                    .orElseThrow(() -> new NotFoundException("Network with id " + dto.getNetworkId() + " not found"));
        } else if(dto.getNetworkName() != null && !dto.getNetworkName().isEmpty()){
            network = this.networkService.findByName(dto.getNetworkName())
                    .orElseThrow(() -> new NotFoundException("Network with name " + dto.getNetworkName() + " not found"));
        } else {
            throw new NotFoundException("Given parameter does not have any unique field set.");
        }
        return DtoMapper.toDto(virtualNetworkService.init(network,dto.getName()));
    }

    @PostMapping("/{virtualNetworkId}")
    public VirtualLayerDto getLayerAtPos(@PathVariable long virtualNetworkId, @RequestBody int pos) throws NotFoundException {
        try {
            return DtoMapper.toDto(virtualNetworkService.getForIdAtPos(virtualNetworkId,pos));
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }

    }

    @GetMapping("/withNetworkId/{networkId}")
    public List<VirtualNetworkDto> getAllForNetworkId(@PathVariable long networkId) throws NotFoundException {
        try {
            return virtualNetworkService.getForNetworkId(networkId).stream()
                    .map(DtoMapper::toDto)
                    .collect(Collectors.toList());
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }
}
