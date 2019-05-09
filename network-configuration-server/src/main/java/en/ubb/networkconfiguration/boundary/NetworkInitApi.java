package en.ubb.networkconfiguration.boundary;

import en.ubb.networkconfiguration.boundary.dto.setup.NetworkInitDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.service.NetworkInitService;
import en.ubb.networkconfiguration.service.NetworkService;
import en.ubb.networkconfiguration.validation.exception.boundary.NetworkAccessException;
import en.ubb.networkconfiguration.validation.exception.boundary.NotFoundException;
import en.ubb.networkconfiguration.validation.exception.boundary.ValidationException;
import en.ubb.networkconfiguration.validation.exception.business.DuplicateBussExc;
import en.ubb.networkconfiguration.validation.exception.business.NetworkAccessBussExc;
import en.ubb.networkconfiguration.validation.validator.NetworkInitDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("network-management")
public class NetworkInitApi {

    private final NetworkService networkService;

    private final NetworkInitService networkInitService;

    private final NetworkInitDtoValidator networkInitDtoValidator;

    @Autowired
    public NetworkInitApi(NetworkService networkService, NetworkInitDtoValidator networkInitDtoValidator, NetworkInitService networkInitService) {
        this.networkService = networkService;
        this.networkInitDtoValidator = networkInitDtoValidator;
        this.networkInitService = networkInitService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(networkInitDtoValidator);
    }

    @PostMapping("init")
    public void create(
            @Valid @RequestBody NetworkInitDto dto,
            BindingResult result,
            SessionStatus status) throws NetworkAccessException, ValidationException {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        try {
            this.networkInitService.create(DtoMapper.fromDto(dto));
            this.networkService.create(DtoMapper.fromDto(dto));
        } catch (NetworkAccessBussExc ex) {
            throw new NetworkAccessException(ex);
        } catch (DuplicateBussExc ex){

        }
        status.setComplete();
    }

    @GetMapping(value = "init", produces = "application/json")
    public List<NetworkInitDto> getAll(){
        return this.networkInitService.getAll().stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "init/{id}", produces = "application/json")
    public NetworkInitDto getById(@NotNull @PathVariable Long id) throws NotFoundException {
        return this.networkInitService.findById(id).map(DtoMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Network initializer not found."));
    }

    @DeleteMapping(value = "init/{id}")
    public void deleteById(@NotNull @PathVariable Long id) throws NotFoundException {
        if (!this.networkInitService.deleteById(id)) {
            throw new NotFoundException("Network initializer not found.");
        }
    }



}
