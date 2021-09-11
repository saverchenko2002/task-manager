package saver.etaskify.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import saver.etaskify.model.Role;

import java.util.Set;

@AllArgsConstructor
@Data
public class JwtResponse {

    private String token;
    private final String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;
}
