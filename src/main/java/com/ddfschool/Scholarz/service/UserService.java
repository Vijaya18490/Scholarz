package com.ddfschool.Scholarz.service;

import com.ddfschool.Scholarz.model.ERole;
import com.ddfschool.Scholarz.model.Role;
import com.ddfschool.Scholarz.model.User;
import com.ddfschool.Scholarz.repository.RoleRepository;
import com.ddfschool.Scholarz.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register user and enforce only one SUPER_ADMIN
    public User registerUser(User user) {
        boolean isSuperAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ROLE_SUPER_ADMIN"));

        if (isSuperAdmin) {
            boolean superAdminExists = userRepository.findAll().stream()
                    .anyMatch(existingUser ->
                            existingUser.getRoles().stream()
                                    .anyMatch(role -> role.getName().name().equals("ROLE_SUPER_ADMIN"))
                    );

            if (superAdminExists) {
                throw new RuntimeException("Only one SUPER_ADMIN is allowed in the system.");
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Convert role strings (from signup) to Role entities
    public Set<Role> convertRoleStringsToRoles(Set<String> roleStrings) {
        Set<Role> roles = new HashSet<>();

        for (String roleStr : roleStrings) {
            ERole erole = ERole.valueOf(roleStr);
            Role role = roleRepository.findByName(erole)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found: " + roleStr));
            roles.add(role);
        }

        return roles;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}