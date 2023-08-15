package com.example.shopBackend.security;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthRequest() {
    }
}
