package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.network.setup.NetworkInitDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.boundary.validation.exception.DuplicateException;
import en.ubb.networkconfiguration.business.service.BranchService;
import en.ubb.networkconfiguration.business.service.NetworkInitService;
import en.ubb.networkconfiguration.business.service.NetworkService;
import en.ubb.networkconfiguration.boundary.validation.exception.NetworkAccessException;
import en.ubb.networkconfiguration.boundary.validation.exception.NotFoundException;
import en.ubb.networkconfiguration.boundary.validation.exception.ValidationException;
import en.ubb.networkconfiguration.business.validation.exception.DuplicateBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NetworkAccessBussExc;
import en.ubb.networkconfiguration.boundary.validation.validator.NetworkInitDtoValidator;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("network-management/init")
public class NetworkInitApi {

    private final NetworkService networkService;

    private final NetworkInitService networkInitService;

    private final BranchService branchService;

    private final NetworkInitDtoValidator networkInitDtoValidator;

    @Autowired
    public NetworkInitApi(NetworkService networkService, NetworkInitDtoValidator networkInitDtoValidator, NetworkInitService networkInitService, BranchService branchService) {
        this.networkService = networkService;
        this.networkInitDtoValidator = networkInitDtoValidator;
        this.networkInitService = networkInitService;
        this.branchService = branchService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(networkInitDtoValidator);
    }

    @PostMapping
    public void create(
            @Valid @RequestBody NetworkInitDto dto,
            BindingResult result,
            SessionStatus status) throws NetworkAccessException, ValidationException, DuplicateException, NotFoundException {

        if (result.hasErrors()) {
            throw new ValidationException(result);
        }

        try {
            this.networkInitService.create(DtoMapper.fromDto(dto));
            this.networkService.create(dto.getBranchId(),DtoMapper.fromDto(dto));

        } catch (NetworkAccessBussExc ex) {
            throw new NetworkAccessException(ex);
        } catch (DuplicateBussExc ex){
            throw new DuplicateException(ex);
        } catch (NotFoundBussExc ex) {
            throw new NotFoundException(ex);
        }
        status.setComplete();
    }

    @GetMapping
    public List<NetworkInitDto> getAll(){
        return this.networkInitService.getAll().stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public NetworkInitDto getById(@NotNull @PathVariable Long id) throws NotFoundException {
        return this.networkInitService.findById(id).map(DtoMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Network initializer not found."));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@NotNull @PathVariable Long id) throws NotFoundException {
        if (!this.networkInitService.deleteById(id)) {
            throw new NotFoundException("Network initializer not found.");
        }
    }



}
