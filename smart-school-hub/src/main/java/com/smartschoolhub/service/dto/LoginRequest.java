package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Username is required")
    @jakarta.validation.constraints.Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @jakarta.validation.constraints.Size(min = 6)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
