package saver.etaskify.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class JwtAuthenticationResponse {

    private String accessToken;
    private final String tokenType = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
}
