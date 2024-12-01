package com.employeemanagement.dto;

public class JwtResponse {
    private String token;
    private final Long userId;

    public JwtResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }
}

