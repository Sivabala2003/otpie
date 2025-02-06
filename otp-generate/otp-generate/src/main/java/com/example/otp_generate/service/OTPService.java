package com.example.otp_generate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public OTPService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    // Method to generate a random OTP
    public int generateOTP() {
        Random random = new Random();
        return 100000 + random.nextInt(900000); // 6-digit OTP
    }

    // Method to send OTP email
    public void sendOTPEmail(String toEmail, int otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);

        emailSender.send(message);
    }
}
