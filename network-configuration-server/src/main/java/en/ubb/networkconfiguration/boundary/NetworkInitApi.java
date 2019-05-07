package en.ubb.networkconfiguration.boundary;

import en.ubb.networkconfiguration.boundary.dto.setup.NetworkInitDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.service.NetworkService;
import en.ubb.networkconfiguration.validation.exception.boundary.NetworkAccessException;
import en.ubb.networkconfiguration.validation.exception.boundary.ValidationException;
import en.ubb.networkconfiguration.validation.exception.business.NetworkAccessBussExc;
import en.ubb.networkconfiguration.validation.validator.NetworkInitDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@RestController
@RequestMapping("network-management")
public class NetworkInitApi {

    private final NetworkService networkService;

    private final NetworkInitDtoValidator networkInitDtoValidator;

    @Autowired
    public NetworkInitApi(NetworkService networkService, NetworkInitDtoValidator networkInitDtoValidator) {
        this.networkService = networkService;
        this.networkInitDtoValidator = networkInitDtoValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(networkInitDtoValidator);
    }

    @PostMapping("/networks/init")
    public void create(
            @Valid @RequestBody NetworkInitDto dto,
            BindingResult result,
            SessionStatus status) throws NetworkAccessException, ValidationException {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        try {
            this.networkService.create(DtoMapper.fromDto(dto));
        } catch (NetworkAccessBussExc ex) {
            throw new NetworkAccessException(ex);
        }
        status.setComplete();
    }

}
