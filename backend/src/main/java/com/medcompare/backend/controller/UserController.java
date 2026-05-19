package com.medcompare.backend.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.medcompare.backend.config.JwtUtil;
import com.medcompare.backend.dto.AuthResponse;
import com.medcompare.backend.model.user;
import com.medcompare.backend.repository.UserRepository;
import com.medcompare.backend.service.UserService;
import java.util.List;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class UserController {

    @Autowired
    private UserService userService;
 
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody user user) {
        userService.register(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");

        return response;
    } 
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
    return "Welcome Admin!";
}



// @PostMapping("/login")
// public Map<String, String> login(@RequestBody user user) {

//     // ✅ Step 1: Validate user (email + password)
//     userService.login(user.getEmail(), user.getPassword());

//     // ✅ Step 2: Generate JWT token
//     String token = JwtUtil.generateToken(user.getEmail());

//     // ✅ Step 3: Send token in response
//     Map<String, String> response = new HashMap<>();
//     response.put("token", token);

//     return response;
// }
@PostMapping("/login")
public AuthResponse login(@RequestBody user user) {
    user dbUser = userService.login(user.getEmail(), user.getPassword());

    String token = JwtUtil.generateToken(dbUser.getEmail(), dbUser.getRole());

    return new AuthResponse(token);
}
    @GetMapping("/test")
    public String test() {
        return "Working!";
    }
    @GetMapping("/profile")
    public String profile() {
       return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    @GetMapping("/admin/users")
public List<user> getAllUsers() {
    return userRepository.findAll();
}
@DeleteMapping("/admin/users/{id}")
public String deleteUser(@PathVariable Long id) {
    userRepository.deleteById(id);
    return "User deleted successfully";
}

}