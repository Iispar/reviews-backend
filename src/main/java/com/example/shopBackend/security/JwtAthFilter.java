package com.example.shopBackend.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Custom authorization check for doFilterInternal
 */
@Component
public class JwtAthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Checks the request and it's authentication
     * @param request
     *        Incoming request
     * @param response
     *        The response
     * @param filterChain
     *        Our filterChain to be used
     */
    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        String username = null;
        final String jwtToken;

        // if no authorization continue with the filterChain
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }


        jwtToken = authHeader.substring(7); // remove the "Bearer " from the header

        try {
            username = jwtService.getUsername(jwtToken); // username from token
        } catch (Exception e) {
            response.sendError(401, e.getMessage());
            return;
        }

        // if no username or user isn't logged in yet
        if (username == null || SecurityContextHolder.getContext().getAuthentication() == null) {
           UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
           // if token has correct username and isn't expired
           if (Boolean.TRUE.equals(jwtService.checkToken(jwtToken, userDetails))) {
               // creates token and sets details and authentication to context.
               UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(token);
           }
        }
        filterChain.doFilter(request, response);
    }
}
