package saver.etaskify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import saver.etaskify.model.Role;
import saver.etaskify.model.RoleName;
import saver.etaskify.model.User;
import saver.etaskify.model.payload.RegistrationRequest;
import saver.etaskify.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User createUser(RegistrationRequest registerRequest) {
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEmail(registerRequest.getEmail());
        newUser.addRole(getRoleFromRequest(registerRequest.getRoles().isEmpty()));
        return newUser;
    }

    private Role getRoleFromRequest(boolean isUser) {
        Optional<Role> role;
        if (isUser) {
            role = roleService.findByRole(RoleName.ROLE_USER);
        } else {
            role = roleService.findByRole(RoleName.ROLE_ADMIN);
        }

        return role.orElseThrow(() ->
                new IllegalArgumentException("Current role with name " + role.get().getRole().name() + " doesn't exist"));
    }
}
