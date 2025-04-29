package com.ddfschool.Scholarz.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Step 1: Retrieve the JWT token from the request
        String token = getJwtFromRequest(request);

        // Step 2: Validate the token (optional)
        if (token != null && jwtUtils.validateJwtToken(token)) {
            // Step 3: Extract username and roles from the token
            String username = jwtUtils.getUsernameFromJwtToken(token);
            List<String> roles = jwtUtils.getRolesFromJwtToken(token);

            // Step 4: Create authentication object with roles (authorities)
            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> (GrantedAuthority) () -> role)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);  // Set the authentication in context
        }

        // Step 5: Continue with the request
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extract the token from the "Bearer <token>" format
        }
        return null;
    }
}
