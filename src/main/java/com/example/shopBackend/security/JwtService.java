package com.example.shopBackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Helpers for Jwt
 */
@Component
public class JwtService {

    // gets secret key from application.properties
    private String SECRET_KEY;

    public JwtService(@Value("${secret.key}") String SECRET_KEY){
        this.SECRET_KEY = SECRET_KEY;
    }


    /**
     * Gets username from token
     * @param token
     *        The token from to be searched from
     * @return Username
     */
    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Get expiration from token
     * @param token
     *        The token from to be searched from
     * @return Expiration date
     */
    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }


    /**
     * Gets the claim that is wanted
     * @param token
     *        Token to be searched from
     * @param claimsResolver
     *        Custom claimsResolver if wanted
     *
     * @return wanted claim
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Returns all claims for token
     * @param token
     *        Token to be used for getting claims
     * @return all Claims
     */
    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Generates new token
     * @param claims
     *        Claims to be used
     * @param userDetails
     *        account userDetails
     * @return token
     */
    public String newToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() * 1000 * 120))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * generates token with no claims.
     * @param userDetails
     *        Accounts userDetails
     * @return token
     */
    public String newToken(UserDetails userDetails) {
        return newToken(new HashMap<>(), userDetails);
    }

    /**
     * Checks tokens username and if it is valid still.
     * @param token
     *        Token to be checked
     * @param userDetails
     *        Accounts userDetails
     * @return true if valid token false otherwise
     */
    public Boolean checkToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if token is expired.
     * @param token
     *        Token to be checked.
     * @return true if valid, false if expired
     */
    public Boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    /**
     * Gets the signing key.
     * @return signing key
     */
    private Key getSigningKey() {
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }
}
