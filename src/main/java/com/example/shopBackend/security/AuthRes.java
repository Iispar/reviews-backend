package com.example.shopBackend.security;

import lombok.Data;

/**
 * The authentication response object
 */

@Data
public class AuthRes {

    private String token;
    private int accountId;

    public AuthRes(String token, int accountId) {
        this.accountId = accountId;
        this.token = token;
    }


}
