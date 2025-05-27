package com.se114p12.backend.configs;

import com.se114p12.backend.services.authentication.LoginAttemptService;
import com.se114p12.backend.util.JwtUtil;
import com.se114p12.backend.util.LoginUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationHandler
    implements AuthenticationFailureHandler, AuthenticationSuccessHandler {

  private final LoginAttemptService loginAttemptService;
  private final JwtUtil jwtUtil;
  private final LoginUtil loginUtil;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException, ServletException {
    String credentialId = request.getParameter("credentialId");
    System.out.println(credentialId);
    Long userId = loginUtil.getUserByCredentialId(credentialId).getId();
    loginAttemptService.loginFailed(userId);
    response.sendError(
        HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + exception.getMessage());
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    Long userId = jwtUtil.getCurrentUserId();
    if (userId != null) {
      loginAttemptService.loginSucceeded(userId);
    }
  }
}
