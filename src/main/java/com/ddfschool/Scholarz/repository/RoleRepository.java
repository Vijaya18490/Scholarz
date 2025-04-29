package com.ddfschool.Scholarz.repository;

import com.ddfschool.Scholarz.model.ERole;
import com.ddfschool.Scholarz.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Automatically derived query method
    Optional<Role> findByName(ERole name);

    // Checks if a role with the given name exists
    boolean existsByName(ERole name);
}
