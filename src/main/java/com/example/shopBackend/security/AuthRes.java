package com.example.shopBackend.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRes {

    private String token;

    public AuthRes(String token) {
        this.token = token;
    }

    public AuthRes() {};
}
