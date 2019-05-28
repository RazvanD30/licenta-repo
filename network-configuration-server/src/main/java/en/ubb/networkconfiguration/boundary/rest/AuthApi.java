package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.authentication.UserDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.business.service.UserService;
import en.ubb.networkconfiguration.business.validation.exception.DuplicateBussExc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user-management")
public class AuthApi {

    private final UserService userService;

    @Autowired
    public AuthApi(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public void createUser(@RequestBody UserDto userDto) throws DuplicateBussExc {
        this.userService.create(userDto.getUsername(), userDto.getPassword(), userDto.getRole());
    }

    @GetMapping(produces = "application/json")
    public List<UserDto> getAll() {
        return this.userService.getAll().stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
