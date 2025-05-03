package com.ddfschool.Scholarz.controller;

import com.ddfschool.Scholarz.dto.PasswordChangeRequest;
import com.ddfschool.Scholarz.model.User;
import com.ddfschool.Scholarz.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Inject dependencies for password encoder and user repository
    public PasswordController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Admin requests password reset (Info message for now)
    @PostMapping("/request-reset")
    //@PreAuthorize("hasRole('ADMIN','USER')") // Only Admin can request
    public String requestReset(@RequestParam String username) {
        // In a real project, this would create a request record to the DB
        return "Reset password requested for user: " + username + ". Waiting for Super Admin to reset.";
    }

    // Super Admin resets password without needing old password
    @PostMapping("/reset")
    @PreAuthorize("hasRole('SUPER_ADMIN')") // Only Super Admin can reset
    public String resetPassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        // Get the username from the PasswordChangeRequest object
        String username = passwordChangeRequest.getUsername();
        // Find user by username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        // Generate a random password
        String newPassword = generateRandomPassword();

        // Encode the password using the password encoder
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Return a response including the new password
        return "Password for user: " + username + " has been changed successfully. New password: " + newPassword;
    }

    // Helper method to generate a random password
    private String generateRandomPassword() {
        int length = 12; // Define the length of the password
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }
}
