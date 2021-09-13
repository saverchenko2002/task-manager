package saver.etaskify.model.payload;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Login Username cannot be blank")
    private String username;

    @NotNull(message = "Login password cannot be blank")
    private String password;
}
