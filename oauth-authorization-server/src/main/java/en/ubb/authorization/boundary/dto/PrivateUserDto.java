package en.ubb.authorization.boundary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import en.ubb.authorization.persistence.domain.authentication.enums.Role;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PrivateUserDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("roles")
    private List<Role> roles;
}
