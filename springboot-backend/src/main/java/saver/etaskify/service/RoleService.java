package saver.etaskify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saver.etaskify.model.Role;
import saver.etaskify.model.RoleName;
import saver.etaskify.repository.RoleRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Collection<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findByRole(RoleName name) {
        return roleRepository.findByRole(name);
    }
}
