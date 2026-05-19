package com.medcompare.backend.service;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPriceDropEmail(String toEmail, String userName,
                                   String medicineName,
                                   Double oldPrice, Double newPrice) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("yourgmail@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Price Drop Alert: " + medicineName + " is now cheaper!");

            double savings = oldPrice - newPrice;

            String html = """
                <div style="font-family:Arial,sans-serif;max-width:500px;margin:auto;
                            padding:20px;border:1px solid #eee;border-radius:10px;">
                    <h2 style="color:#1a5c32;">Price dropped! 🎉</h2>
                    <p>Hi %s,</p>
                    <p>Good news! <strong>%s</strong> that you were watching has dropped in price.</p>
                    <table style="width:100%%;border-collapse:collapse;margin:16px 0;">
                        <tr>
                            <td style="padding:8px;color:gray;">Your subscribed price</td>
                            <td style="padding:8px;"><s>₹%.2f</s></td>
                        </tr>
                        <tr>
                            <td style="padding:8px;color:gray;">New lowest price</td>
                            <td style="padding:8px;color:#1a5c32;font-weight:bold;font-size:18px;">
                                ₹%.2f
                            </td>
                        </tr>
                        <tr>
                            <td style="padding:8px;color:gray;">You save</td>
                            <td style="padding:8px;color:green;font-weight:bold;">₹%.2f</td>
                        </tr>
                    </table>
                    <a href="http://yourwebsite.com"
                       style="display:inline-block;background:#1a5c32;color:white;
                              padding:10px 20px;border-radius:6px;text-decoration:none;">
                        View Price Comparison →
                    </a>
                    <p style="margin-top:20px;font-size:12px;color:gray;">
                        You received this because you set a price alert on our platform.
                    </p>
                </div>
            """.formatted(
                userName != null ? userName : "there",
                medicineName,
                oldPrice,
                newPrice,
                savings
            );

            helper.setText(html, true); // true = HTML
            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("Email send failed: " + e.getMessage());
        }
    }
}