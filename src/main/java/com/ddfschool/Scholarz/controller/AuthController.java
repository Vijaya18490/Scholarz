package com.ddfschool.Scholarz.controller;

import com.ddfschool.Scholarz.dto.JwtResponse;
import com.ddfschool.Scholarz.dto.LoginRequest;
import com.ddfschool.Scholarz.dto.SignupRequest;
import com.ddfschool.Scholarz.model.User;
import com.ddfschool.Scholarz.service.UserService;
import com.ddfschool.Scholarz.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByUsername(loginRequest.getUsername());

        String jwt = jwtUtils.generateJwtToken(
                user.getUsername(),
                user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        try {
            User user = new User();
            user.setFirstName(signupRequest.getFirstName());
            user.setLastName(signupRequest.getLastName());
            user.setUsername(signupRequest.getUsername());
            user.setPassword(signupRequest.getPassword());  // Password will be hashed in service
            user.setRoles(userService.convertRoleStringsToRoles(signupRequest.getRoles()));

            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}
