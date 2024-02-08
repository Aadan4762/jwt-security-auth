package com.adan.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  // JWT Authentication filter to authenticate requests
  private final JwtAuthenticationFilter jwtAuthFilter;

  // Authentication provider for custom authentication logic
  private final AuthenticationProvider authenticationProvider;

  // Logout handler to manage user logout
  private final LogoutHandler logoutHandler;

  @Bean
  // Configuring security filter chain for HTTP requests
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf()
            .disable()
            .authorizeHttpRequests()
            // Permit access to authentication endpoints
            .requestMatchers("/api/v1/authentication/**").permitAll()
            // Permit access to product creation endpoint
            .requestMatchers("/api/v1/products/create").permitAll()
            // Permit access to manufacturer endpoints
            .requestMatchers("/api/v2/manufacturers/**").permitAll()
            // Require authentication for any other requests
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // Set custom authentication provider
            .authenticationProvider(authenticationProvider)
            // Add JWT authentication filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            // Configure logout
            .logout()
            .logoutUrl("/api/v1/authentication/logout")
            .addLogoutHandler(logoutHandler)
            // Clear security context on successful logout
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
    return http.build();
  }
}
