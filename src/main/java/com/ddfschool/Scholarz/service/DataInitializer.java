package com.ddfschool.Scholarz.service;

import com.ddfschool.Scholarz.model.ERole;
import com.ddfschool.Scholarz.model.Role;
import com.ddfschool.Scholarz.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if roles already exist, if not, create them
        if (!roleRepository.existsByName(ERole.ROLE_USER)) {
            Role userRole = new Role(ERole.ROLE_USER);
            roleRepository.save(userRole);
        }
        if (!roleRepository.existsByName(ERole.ROLE_ADMIN)) {
            Role adminRole = new Role(ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);
        }
    }
}
