package com.medcompare.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.List;

import java.io.IOException;


public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {
                String email = JwtUtil.extractEmail(token);
                String role = JwtUtil.extractRole(token);
                System.out.println("Email: " + email);
                System.out.println("Role: " + role);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                      email,
                      null,
                List.of(new SimpleGrantedAuthority(role))
    );
        SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                System.out.println("Invalid Token" + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}