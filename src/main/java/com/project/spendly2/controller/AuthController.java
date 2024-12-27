package com.project.spendly2.controller;

import com.project.spendly2.dto.requests.LoginRequest;
import com.project.spendly2.dto.requests.RegisterDto;
import com.project.spendly2.dto.responses.ApiResponse;
import com.project.spendly2.dto.responses.AuthResponse;
import com.project.spendly2.services.AuthServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spendly/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServices authServices;

    @PostMapping("/register")
    public ApiResponse<String> registerUser(@RequestBody RegisterDto registerDto, HttpServletRequest request){
        ApiResponse<String> response = authServices.registerUser(registerDto, request);
        return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> loginUser (@RequestBody LoginRequest loginRequest){
        ApiResponse<AuthResponse> response = authServices.loginUser(loginRequest);
        return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyEmail(@RequestParam String email, @RequestParam String otp, HttpSession session){
        ApiResponse<String> response = authServices.verifyEmail(email, otp, session);
        return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }

    @PostMapping("/resend")
    public ApiResponse<String> resendOtp (@RequestParam String email, HttpSession session){
        ApiResponse<String> response = authServices.resendOtp(email, session);
        return new ApiResponse<>(response.getMessage(), response.getCode());
    }


}
