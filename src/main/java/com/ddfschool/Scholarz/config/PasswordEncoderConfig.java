package com.ddfschool.Scholarz.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner run(PasswordEncoder passwordEncoder) {
        return args -> {
            String rawPassword = "superadmin123";  // <--- Replace with your real SUPER_ADMIN password
            String hashedPassword = passwordEncoder.encode(rawPassword);
            System.out.println("Hashed Password: " + hashedPassword);
        };
    }
}
