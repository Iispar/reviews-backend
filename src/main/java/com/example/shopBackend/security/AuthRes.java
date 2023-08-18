package com.example.shopBackend.security;

import lombok.Data;

/**
 * The authentication response object
 */

@Data
public class AuthRes {

    private String token;

    public AuthRes(String token) {
        this.token = token;
    }


}
