package com.ddfschool.Scholarz.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String username;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    private String password;
    private Set<String> roles; // Expect roles like ["ROLE_USER", "ROLE_SUPER_ADMIN"]
}
