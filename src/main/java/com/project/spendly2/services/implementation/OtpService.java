package com.project.spendly2.services.implementation;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OtpService {


    public String generateOtp(HttpSession httpSession){
        String otp = UUID.randomUUID().toString().substring(0,6);

        httpSession.setAttribute("otp", otp);
        httpSession.setMaxInactiveInterval(300);

        return otp;
    }

    public boolean verifyOtp(HttpSession httpSession, String providedOtp){
        String storedOtp = (String) httpSession.getAttribute("otp");

        if (storedOtp == null){
            return false;
        }
        boolean isValid = storedOtp.equals(providedOtp);

        if (isValid){
            httpSession.removeAttribute("otp");
        }

        return isValid;
    }
}
