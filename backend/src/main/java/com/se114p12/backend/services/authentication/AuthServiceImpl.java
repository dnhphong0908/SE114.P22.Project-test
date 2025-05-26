package com.se114p12.backend.services.authentication;

import com.se114p12.backend.dto.authentication.AuthResponseDTO;
import com.se114p12.backend.dto.authentication.ForgotPasswordRequestDTO;
import com.se114p12.backend.dto.authentication.LoginRequestDTO;
import com.se114p12.backend.dto.authentication.PasswordChangeDTO;
import com.se114p12.backend.dto.authentication.RefreshTokenRequestDTO;
import com.se114p12.backend.dto.authentication.SendOTPRequestDTO;
import com.se114p12.backend.dto.authentication.VerifyOTPRequestDTO;
import com.se114p12.backend.entities.authentication.RefreshToken;
import com.se114p12.backend.entities.authentication.Verification;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.enums.VerificationType;
import com.se114p12.backend.exception.BadRequestException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.authentication.UserRepository;
import com.se114p12.backend.services.general.MailService;
import com.se114p12.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtUtil jwtUtil;
  private final RefreshTokenService refreshTokenService;
  private final UserRepository userRepository;
  private final VerificationService verificationService;
  private final MailService mailService;

  @Override
  public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            loginRequestDTO.getCredentialId(), loginRequestDTO.getPassword());

    Authentication authentication =
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    Long userId = jwtUtil.getCurrentUserId();

    String accessToken = jwtUtil.generateAccessToken(userId);

    String refreshToken = refreshTokenService.generateRefreshToken(userId).getToken();

    AuthResponseDTO loginResponseDTO = new AuthResponseDTO();
    loginResponseDTO.setAccessToken(accessToken);
    loginResponseDTO.setRefreshToken(refreshToken);

    return loginResponseDTO;
  }

  @Override
  public AuthResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
    RefreshToken refreshToken =
        refreshTokenService.verifyExpiration(
            refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken()));

    String accessToken = jwtUtil.generateAccessToken(refreshToken.getUser().getId());

    AuthResponseDTO loginResponseDTO = new AuthResponseDTO();
    loginResponseDTO.setAccessToken(accessToken);
    loginResponseDTO.setRefreshToken(refreshTokenRequestDTO.getRefreshToken());
    return loginResponseDTO;
  }

  @Override
  public void logout(RefreshTokenRequestDTO refreshTokenRequestDTO) {
    refreshTokenService.deleteByToken(refreshTokenRequestDTO.getRefreshToken());
  }

  @Override
  public void verifyEmail(String code) {
    Verification verification =
        verificationService.verifyVerificationCode(code, VerificationType.ACTIVATION);
    User user = verification.getUser();
    user.setStatus(UserStatus.ACTIVE);
    userRepository.save(user);
    verificationService.deleteVerification(verification);
  }

  @Override
  public void resetPassword(PasswordChangeDTO passwordChangeDTO) {
    Long userId = jwtUtil.getCurrentUserId();
    User currentUser =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    if (!passwordEncoder.matches(
        passwordChangeDTO.getCurrentPassword(), currentUser.getPassword())) {
      throw new BadRequestException("Password doesn't match");
    }
    currentUser.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
  }

  @Override
  public void sendOtp(SendOTPRequestDTO sendOTPRequestDTO) {
    User user =
        userRepository
            .findByEmail(sendOTPRequestDTO.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    String otp =
        verificationService.createOtpVerification(user.getId(), sendOTPRequestDTO.getAction());
    mailService.sendOtpEmail(user.getEmail(), otp);
  }

  @Override
  public String verifyOtp(VerifyOTPRequestDTO verifyOTPRequestDTO) {
    String code = verifyOTPRequestDTO.getOtp() + "_" + verifyOTPRequestDTO.getAction();
    Verification verification =
        verificationService.verifyVerificationCode(code, VerificationType.OTP);
    verificationService.deleteVerification(verification);
    switch (verifyOTPRequestDTO.getAction()) {
      case FORGOT_PASSWORD:
        Verification resetPasswordVerification =
            verificationService.createResetPasswordVerification(verification.getUser().getId());
        return resetPasswordVerification.getCode();
      default:
        throw new BadRequestException("Invalid action for OTP verification");
    }
  }

  @Override
  public void forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
    Verification verification =
        verificationService.verifyVerificationCode(
            forgotPasswordRequestDTO.getCode(), VerificationType.RESET_PASSWORD);
    User user = verification.getUser();
    user.setPassword(passwordEncoder.encode(forgotPasswordRequestDTO.getNewPassword()));
    userRepository.save(user);
  }
}
