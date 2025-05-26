package com.se114p12.backend.services.authentication;

import com.se114p12.backend.dto.authentication.AuthResponseDTO;
import com.se114p12.backend.dto.authentication.ForgotPasswordRequestDTO;
import com.se114p12.backend.dto.authentication.LoginRequestDTO;
import com.se114p12.backend.dto.authentication.PasswordChangeDTO;
import com.se114p12.backend.dto.authentication.RefreshTokenRequestDTO;
import com.se114p12.backend.dto.authentication.SendOTPRequestDTO;
import com.se114p12.backend.dto.authentication.VerifyOTPRequestDTO;

public interface AuthService {

  AuthResponseDTO login(LoginRequestDTO loginRequestDTO);

  AuthResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);

  void logout(RefreshTokenRequestDTO refreshTokenRequestDTO);

  void verifyEmail(String code);

  void resetPassword(PasswordChangeDTO passwordChangeDTO);

  void sendOtp(SendOTPRequestDTO sendOTPRequestDTO);

  String verifyOtp(VerifyOTPRequestDTO verifyOTPRequestDTO);

  void forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO);
}
