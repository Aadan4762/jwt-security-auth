package com.adan.security.auth;

import com.adan.security.config.JwtService;
import com.adan.security.token.Token;
import com.adan.security.token.TokenRepository;
import com.adan.security.token.TokenType;
import com.adan.security.user.Role;
import com.adan.security.user.User;
import com.adan.security.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
/**
 * This class provides methods for user authentication, registration, and token management.
 * It utilizes repositories for user and token operations, password encoding, JWT service for token generation,
 * and authentication manager for authenticating user credentials.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  // Dependencies
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  /**
   * Registers a new user with the provided details.
   * @param request The registration request containing user details.
   * @return AuthenticationResponse containing access token and refresh token.
   */
  public AuthenticationResponse register(RegisterRequest request) {
    // Create a new user object with encoded password
    var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();

    // Save the user to the repository
    var savedUser = userRepository.save(user);

    // Generate JWT tokens for the user
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    // Save the user token
    saveUserToken(savedUser, jwtToken);

    // Return authentication response with tokens
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  /**
   * Authenticates the user with the provided credentials.
   * @param request The authentication request containing user credentials.
   * @return AuthenticationResponse containing access token and refresh token.
   */
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    // Authenticate user credentials
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    // Fetch user details from repository
    var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(); // Throw exception if user not found

    // Generate JWT tokens for the user
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    // Revoke existing user tokens
    revokeAllUserTokens(user);

    // Save new user token
    saveUserToken(user, jwtToken);

    // Return authentication response with tokens
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  /**
   * Saves the user token to the repository.
   * @param user The user associated with the token.
   * @param jwtToken The JWT token to be saved.
   */
  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  /**
   * Revokes all valid tokens associated with the user.
   * @param user The user whose tokens need to be revoked.
   */
  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  /**
   * Refreshes the access token using the refresh token.
   * @param request The HTTP request containing the refresh token.
   * @param response The HTTP response where the new tokens will be written.
   * @throws IOException if an I/O error occurs while writing the response.
   */
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.userRepository.findByEmail(userEmail)
              .orElseThrow(); // Throw exception if user not found
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
