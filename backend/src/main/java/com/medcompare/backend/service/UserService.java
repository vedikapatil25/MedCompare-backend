package com.medcompare.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.medcompare.backend.model.user;
import com.medcompare.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public user register(user user) {

        // Optional but important
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Encrypt password
        String encodedPassword = encoder.encode(user.getPassword());
        // Set encrypted password
        user.setPassword(encodedPassword);

        user.setRole("USER");

        return userRepository.save(user);
    }

    public user login(String email, String password) {
        user user = userRepository.findByEmail(email);
        System.out.println("Entered Email: " + email);
        System.out.println("Entered Password: " + password);

        

        if (user == null) {
            System.out.println("User not found");
        } else {
            System.out.println("DB Password: " + user.getPassword());
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        

        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }

        throw new RuntimeException("Invalid email or password");
    }
}