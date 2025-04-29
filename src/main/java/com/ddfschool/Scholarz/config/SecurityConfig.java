package com.ddfschool.Scholarz.config;

import com.ddfschool.Scholarz.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/public/**").permitAll() // Public endpoints
                        .requestMatchers("/api/auth/login").permitAll() // Only login is public
                        .requestMatchers("/api/auth/signup/**").hasRole("SUPER_ADMIN") // Signup needs SUPER_ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Admin-specific endpoints
                        .requestMatchers("/api/password/**").hasRole("SUPER_ADMIN") // Securing the password API for SUPER_ADMIN
                        .requestMatchers("/h2-console/**").permitAll() // Allow H2 console access
                        .requestMatchers("/favicon.ico", "/error").permitAll() // Allow favicon and error page
                        .anyRequest().authenticated() // All other endpoints require authentication
                )
                .csrf(csrf -> csrf.disable()) // Disable CSRF for the sake of the API
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable()) // Disable frame options for H2 console
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
