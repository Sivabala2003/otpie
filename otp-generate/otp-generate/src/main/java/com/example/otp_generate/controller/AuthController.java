package com.example.otp_generate.controller;

import com.example.otp_generate.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OTPService otpService;

    private final Map<String, Integer> otpMap = new HashMap<>(); // For storing OTPs temporarily
    private final Map<String, Boolean> emailVerified = new HashMap<>(); // For storing email verification status

    // Endpoint for signup and OTP generation
    @PostMapping("/signup")
    public String signup(@RequestParam String email) {
        if (emailVerified.getOrDefault(email, false)) {
            return "Email already verified. Please login.";
        }

        // Generate OTP
        int otp = otpService.generateOTP();

        otpMap.put(email, otp);        // Save OTP temporarily


        // Send OTP to email

        otpService.sendOTPEmail(email, otp);

        return "OTP sent to your email. Please verify it.";
    }

    // Endpoint for OTP verification
    @PostMapping("/verify")
    public String verifyOTP(@RequestParam String email, @RequestParam int otp) {
        // Check if OTP matches
        if (otpMap.getOrDefault(email, -1) == otp) {
            // OTP verified
            emailVerified.put(email, true);
            otpMap.remove(email); // OTP used, remove it

            return "OTP verified successfully. You can now login.";
        }

        return "Invalid OTP. Please try again.";
    }

    // Endpoint for login (after OTP verification)
    @PostMapping("/login")
    public String login(@RequestParam String email) {
        if (emailVerified.getOrDefault(email, false)) {
            return "Login successful!";
        }

        return "Email not verified. Please sign up and verify your email.";
    }
}