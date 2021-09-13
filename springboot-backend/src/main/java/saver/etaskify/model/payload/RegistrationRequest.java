package saver.etaskify.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationRequest {

    @NotBlank(message = "Registration username cannot be blank")
    private String username;

    @NotBlank(message = "Registration email cannot be blank")
    private String email;

    @NotNull(message = "Specify whether the user has to be registered as an admin or not")
    private Set<String> roles;

    @NotNull(message = "Registration password cannot be null")
    private String password;
}
