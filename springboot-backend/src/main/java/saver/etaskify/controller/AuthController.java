package saver.etaskify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import saver.etaskify.exception.UserLoginException;
import saver.etaskify.exception.UserRegistrationException;
import saver.etaskify.model.User;
import saver.etaskify.model.payload.JwtAuthenticationResponse;
import saver.etaskify.model.payload.LoginRequest;
import saver.etaskify.model.payload.ApiResponse;
import saver.etaskify.model.payload.RegistrationRequest;
import saver.etaskify.security.JwtTokenProvider;
import saver.etaskify.model.CustomUserDetails;
import saver.etaskify.service.AuthService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/checkEmailInUse")
    public ResponseEntity<ApiResponse> checkEmailInUse(@RequestParam("email") String email) {
        Boolean emailExists = authService.emailAlreadyExists(email);
        return ResponseEntity.ok(new ApiResponse(true, emailExists.toString()));
    }

    @GetMapping("/checkUsernameInUse")
    public ResponseEntity<ApiResponse> checkUsernameInUse(@RequestParam("username") String username) {
        Boolean usernameExists = authService.usernameAlreadyExists(username);
        return ResponseEntity.ok(new ApiResponse(true, usernameExists.toString()));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        Optional<User> registeredUser = authService.registerUser(registrationRequest);
        User userToString = registeredUser.orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(),
                "Couldn't register user " + registrationRequest.getUsername()));
        return ResponseEntity.ok(new ApiResponse(true, userToString.toString() + " -> User CREATED"));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authService.authenticateUser(loginRequest)
                .orElseThrow(() -> new UserLoginException("Couldn't login user " + loginRequest.getUsername()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String jwt = jwtTokenProvider.generateToken(customUserDetails);
        Set<String> roles = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, customUserDetails.getId(),
                customUserDetails.getUsername(), customUserDetails.getEmail(), roles));
    }
}
