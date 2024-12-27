package com.project.spendly2.services;

import com.project.spendly2.dto.requests.LoginRequest;
import com.project.spendly2.dto.requests.RegisterDto;
import com.project.spendly2.dto.responses.ApiResponse;
import com.project.spendly2.dto.responses.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface AuthServices {
     ApiResponse<String> registerUser(RegisterDto registerDto, HttpServletRequest request);
     ApiResponse<AuthResponse> loginUser(LoginRequest login);
     ApiResponse<String> verifyEmail(String email, String otp, HttpSession session);

     String createTransactionPin(String pin);

     ApiResponse<String> resendOtp(String email, HttpSession session);

}
