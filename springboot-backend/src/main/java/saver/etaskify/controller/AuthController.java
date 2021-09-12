package saver.etaskify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import saver.etaskify.model.Role;
import saver.etaskify.model.RoleName;
import saver.etaskify.model.User;
import saver.etaskify.pojo.JwtResponse;
import saver.etaskify.pojo.LoginRequest;
import saver.etaskify.pojo.MessageResponse;
import saver.etaskify.pojo.SignupRequest;
import saver.etaskify.repository.RoleRepository;
import saver.etaskify.repository.UserRepository;
import saver.etaskify.security.JwtTokenProvider;
import saver.etaskify.security.UserDetailsImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        System.out.println(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtTokenProvider.generateToken(userDetails);
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {

        System.out.println(signupRequest);
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username " + signupRequest.getUsername() + " already exists"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User with email " + signupRequest.getEmail() + " already exists"));
        }

        Set<String> requestRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (requestRoles == null) {
            Role userRole = roleRepository
                    .findByRole(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role USER is not found"));
            roles.add(userRole);
        } else {
            Role adminRole = roleRepository
                    .findByRole(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found"));
            roles.add(adminRole);
        }

        userRepository.save(createUserFromRequest(signupRequest, roles));
        System.out.println(userRepository.findByUsername(signupRequest.getUsername()).get());
        return ResponseEntity.ok(new MessageResponse("User CREATED"));
    }

    private User createUserFromRequest(SignupRequest signupRequest, Set<Role> roles) {
        return new User(signupRequest.getEmail(), signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()), roles);
    }


}
