package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.authentication.PrivateUserDto;
import en.ubb.networkconfiguration.business.service.UserService;
import en.ubb.networkconfiguration.business.validation.exception.DuplicateBussExc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Base64;

@RestController
@RequestMapping("authentication")
public class AuthApi {

    private final UserService userService;

    @Autowired
    public AuthApi(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public boolean login(@RequestBody PrivateUserDto userDto) {
        return userService.credentialsMatch(userDto.getUsername(), userDto.getPassword());
    }

    @GetMapping("/user")
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization")
                .substring("Basic".length()).trim();
        return () -> new String(Base64.getDecoder().decode(authToken)).split(":")[0];
    }

    @PostMapping("/register")
    public void register(@RequestBody PrivateUserDto userDto) throws DuplicateBussExc {
        userService.create(userDto.getUsername(), userDto.getPassword(), userDto.getRoles());
    }
}
