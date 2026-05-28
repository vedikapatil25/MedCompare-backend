package com.medcompare.backend.controller;

import com.medcompare.backend.dto.PriceAlertRequestDTO;
import com.medcompare.backend.dto.PriceAlertResponseDTO;
import com.medcompare.backend.model.user;
import com.medcompare.backend.repository.UserRepository;
import com.medcompare.backend.service.PriceAlertService;
import com.medcompare.backend.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class PriceAlertController {

    @Autowired
    private PriceAlertService alertService;

    @Autowired
    private UserRepository userRepository;


    static class AlertRequest {
        public Long medicineId;
        public Double priceAtSubscribe;
    }

    // ---- SUBSCRIBE → POST /api/alerts/subscribe ----
    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(
            @RequestBody AlertRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // Get user from JWT token
        String token = authHeader.replace("Bearer ", "");
        String userEmail = JwtUtil.extractEmail(token);

        user existingUser = userRepository.findByEmail(userEmail);
        if (existingUser == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // Build DTO
        PriceAlertRequestDTO dto = new PriceAlertRequestDTO();
        dto.setUserId(existingUser.getId());
        dto.setMedicineId(request.medicineId);
        dto.setPriceAtSubscribe(request.priceAtSubscribe);

        PriceAlertResponseDTO response = alertService.createAlert(dto);

        // Already subscribed
        if (response.getMessage() != null) {
            return ResponseEntity.status(409).body(response.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // ---- UNSUBSCRIBE → DELETE /api/alerts/unsubscribe ----
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(
            @RequestBody AlertRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String userEmail = JwtUtil.extractEmail(token);

        user existingUser = userRepository.findByEmail(userEmail);
        if (existingUser == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        alertService.cancelAlertByUserAndMedicine(existingUser.getId(), request.medicineId);
        return ResponseEntity.ok("Alert removed");
    }

    // ---- GET MY ALERTS → GET /api/alerts/my-alerts ----
    @GetMapping("/my-alerts")
    public ResponseEntity<List<PriceAlertResponseDTO>> getMyAlerts(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String userEmail = JwtUtil.extractEmail(token);

        user existingUser = userRepository.findByEmail(userEmail);
        if (existingUser == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(alertService.getAlertsByUser(existingUser.getId()));
    }
}