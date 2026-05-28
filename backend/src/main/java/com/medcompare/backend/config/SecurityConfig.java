package com.medcompare.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
public class SecurityConfig {
@Bean
public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(List.of("http://127.0.0.1:5501"  ,
    "http://localhost:5501",
    "http://127.0.0.1:5500",
    "http://localhost:5500"
)); // frontend
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
}
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .cors(cors -> {})
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/users/login", "/api/users/register").permitAll()
            .requestMatchers("/images/**").permitAll()
            .requestMatchers("/api/users/forgot-password").permitAll()
            .requestMatchers("/api/users/reset-password").permitAll()
            // .requestMatchers(HttpMethod.GET, "/api/medicines/**").permitAll()
            // .requestMatchers(HttpMethod.POST, "/api/medicines/**").hasAuthority("ADMIN")
            .requestMatchers("/api/medicines/**").permitAll()   // for addig  medicines
            .requestMatchers("/api/alerts/**").permitAll() 
            .requestMatchers("/api/users/admin/**").hasAuthority("ADMIN")
            // .requestMatchers("/api/users/admin/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
}