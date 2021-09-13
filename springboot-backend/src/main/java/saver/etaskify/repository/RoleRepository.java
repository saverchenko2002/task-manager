package saver.etaskify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import saver.etaskify.model.Role;
import saver.etaskify.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleName name);
}
