package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.authentication.PublicUserDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user-management")
public class UserApi {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<PublicUserDto> getAll(){
        return userService.getAll().stream()
                .map(DtoMapper::toPublicDto)
                .collect(Collectors.toList());
    }
}
