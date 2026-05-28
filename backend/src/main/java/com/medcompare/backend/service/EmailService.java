package com.medcompare.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetLink = baseUrl + "/reset-password.html?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("MedCompare - Reset Your Password");
        message.setText(
            "Hello,\n\n" +
            "You requested a password reset for your MedCompare account.\n\n" +
            "Click the link below to reset your password:\n" +
            resetLink + "\n\n" +
            "This link will expire in 15 minutes.\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "— MedCompare Team"
        );

        mailSender.send(message);
    }
    // ---- PRICE DROP ALERT EMAIL ----
    public void sendPriceDropEmail(String toEmail, String medicineName,
                                    double oldPrice, double newPrice) {
        double savings = oldPrice - newPrice;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("💊 Price Drop Alert - " + medicineName);
        message.setText(
            "Good news!\n\n" +
            "The price of " + medicineName + " has dropped!\n\n" +
            "Old Price : ₹" + oldPrice + "\n" +
            "New Price : ₹" + newPrice + "\n" +
            "You Save  : ₹" + String.format("%.2f", savings) + "\n\n" +
            "Visit MedCompare to buy at the best price.\n\n" +
            "— MedCompare Team"
        );

        mailSender.send(message);
    }

}