package saver.etaskify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import saver.etaskify.exception.ResourceAlreadyInUseException;
import saver.etaskify.model.User;
import saver.etaskify.model.payload.LoginRequest;
import saver.etaskify.model.payload.RegistrationRequest;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public Optional<User> registerUser(RegistrationRequest newRegistrationRequest) {
        String newRegistrationRequestEmail = newRegistrationRequest.getEmail();
        String newRegistrationRequestUsername = newRegistrationRequest.getUsername();
        if (emailAlreadyExists(newRegistrationRequestEmail)) {
            throw new ResourceAlreadyInUseException("Email", "Address", newRegistrationRequestEmail);
        } else if (usernameAlreadyExists(newRegistrationRequestUsername)) {
            throw new ResourceAlreadyInUseException("Username", "User", newRegistrationRequestUsername);
        }
        User newUser = userService.createUser(newRegistrationRequest);
        User registeredNewUser = userService.save(newUser);
        return Optional.ofNullable(registeredNewUser);
    }

    public Boolean usernameAlreadyExists(String username) {
        return userService.existsByUsername(username);
    }

    public Boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }

    public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
        return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword())));
    }
}
