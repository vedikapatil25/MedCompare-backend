package com.medcompare.backend.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.medcompare.backend.model.PasswordResetToken;
import com.medcompare.backend.model.user;
import com.medcompare.backend.repository.PasswordResetTokenRepository;
import com.medcompare.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    // ---- FORGOT PASSWORD ----
    @Transactional
    public void forgotPassword(String email) {
        user existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            throw new RuntimeException("Email not found");
        }

        tokenRepository.deleteByEmail(email);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(60);

        PasswordResetToken resetToken = new PasswordResetToken(token, email, expiry);
        tokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(email, token);
    }

    // ---- RESET PASSWORD ----
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Reset token has expired");
        }

        user existingUser = userRepository.findByEmail(resetToken.getEmail());
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        existingUser.setPassword(encoder.encode(newPassword));
        userRepository.save(existingUser);
        tokenRepository.delete(resetToken);
    }

    // ---- REGISTER ----
    public user register(user newUser) {
        if (userRepository.findByEmail(newUser.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        newUser.setRole("USER");

        return userRepository.save(newUser);
    }

    // ---- LOGIN ----
    public user login(String email, String password) {
        user existingUser = userRepository.findByEmail(email);
        System.out.println("Entered Email: " + email);
        System.out.println("Entered Password: " + password);

        if (existingUser == null) {
            System.out.println("User not found");
        } else {
            System.out.println("DB Password: " + existingUser.getPassword());
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (existingUser != null && encoder.matches(password, existingUser.getPassword())) {
            return existingUser;
        }

        throw new RuntimeException("Invalid email or password");
    }
}