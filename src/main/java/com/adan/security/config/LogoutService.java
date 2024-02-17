package com.adan.security.config;

import com.adan.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private final TokenRepository tokenRepository;

  // This method handles the logout process.
  @Override
  public void logout(
          HttpServletRequest request,
          HttpServletResponse response,
          Authentication authentication
  ) {
    // Extract the JWT token from the request header.
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    // If the Authorization header is missing or does not start with "Bearer ", return.
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    // Extract the JWT token from the Authorization header.
    jwt = authHeader.substring(7);
    // Find the token from the repository based on the extracted JWT.
    var storedToken = tokenRepository.findByToken(jwt)
            .orElse(null);
    // If the token exists, mark it as expired and revoked, then save the changes.
    if (storedToken != null) {
      storedToken.setExpired(true);
      storedToken.setRevoked(true);
      tokenRepository.save(storedToken);
      // Clear the security context to ensure the user is logged out.
      SecurityContextHolder.clearContext();
    }
  }
}