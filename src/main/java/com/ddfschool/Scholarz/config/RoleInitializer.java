package com.ddfschool.Scholarz.config;

import com.ddfschool.Scholarz.model.ERole;
import com.ddfschool.Scholarz.model.Role;
import com.ddfschool.Scholarz.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        for (ERole roleEnum : ERole.values()) {
            if (!roleRepository.existsByName(roleEnum)) {
                Role role = new Role();
                role.setName(roleEnum);
                roleRepository.save(role);
                System.out.println("Inserted missing role: " + roleEnum);
            }
        }
    }
}
