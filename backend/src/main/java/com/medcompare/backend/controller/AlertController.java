package com.medcompare.backend.controller;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import com.medcompare.backend.config.JwtUtil;
import com.medcompare.backend.entity.PriceAlert;
import com.medcompare.backend.entity.Medicine;
import com.medcompare.backend.model.user;
import com.medcompare.backend.repository.MedicineRepository;
import com.medcompare.backend.repository.PriceAlertRepository;
import com.medcompare.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {


    @Autowired
    private PriceAlertRepository alertRepo;

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private UserRepository userRepo;

    // POST /api/alerts/subscribe
    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody Map<String, Long> body,
                                       HttpServletRequest request) {

        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }

        Long medicineId = body.get("medicineId");

        // Check already subscribed
        Optional<PriceAlert> existing = alertRepo.findByUserIdAndMedicineId(userId, medicineId);
        if (existing.isPresent()) {
            return ResponseEntity.status(409).body(Map.of("message", "Already subscribed"));
        }

        // Get medicine
        Medicine medicine = medicineRepo.findById(medicineId).orElse(null);
        if (medicine == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Medicine not found"));
        }

        double lowestPrice = getLowestPrice(medicine);

        PriceAlert alert = new PriceAlert();
        alert.setUserId(userId);
        alert.setMedicineId(medicineId);
        alert.setPriceAtSubscribe(lowestPrice);
        alertRepo.save(alert);

        return ResponseEntity.ok(Map.of("message", "Alert set successfully"));
    }

    // DELETE /api/alerts/unsubscribe
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestBody Map<String, Long> body,
                                          HttpServletRequest request) {

        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }

        Long medicineId = body.get("medicineId");

        alertRepo.findByUserIdAndMedicineId(userId, medicineId)
                 .ifPresent(alertRepo::delete);

        return ResponseEntity.ok(Map.of("message", "Alert removed"));
    }

    // ── Helpers ───────────────────────────────────────────────

    private double getLowestPrice(Medicine medicine) {
        return Stream.of(
                medicine.getApolloPrice(),
                medicine.getOnemgPrice(),
                medicine.getPharmeasyPrice()
            )
            .filter(p -> p != null && p > 0)
            .mapToDouble(Double::doubleValue)
            .min()
            .orElse(0.0);
    }

    private Long getUserIdFromToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

            String token = authHeader.replace("Bearer ", "");

            // Step 1: extract email from token
            String email = JwtUtil.extractEmail(token);

            // Step 2: find user by email and return their ID
            user u = userRepo.findByEmail(email);
            if (u == null) return null;

            return u.getId();

        } catch (Exception e) {
            return null;
        }
    }
}