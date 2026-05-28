package com.medcompare.backend.service;

import com.medcompare.backend.dto.PriceAlertRequestDTO;
import com.medcompare.backend.dto.PriceAlertResponseDTO;
import com.medcompare.backend.entity.PriceAlert;
import com.medcompare.backend.model.user;
import com.medcompare.backend.repository.PriceAlertRepository;
import com.medcompare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriceAlertService {

    @Autowired
    private PriceAlertRepository alertRepository;

    @Autowired
    private UserRepository userRepository;        // ✅ added

    @Autowired
    private EmailService emailService;            // ✅ added

    // ---- CANCEL BY USER + MEDICINE ----
    @Transactional
    public void cancelAlertByUserAndMedicine(Long userId, Long medicineId) {
        alertRepository.findByUserIdAndMedicineId(userId, medicineId)
            .ifPresent(alert -> {
                alert.setIsActive(false);
                alertRepository.save(alert);
            });
    }

    // ---- CREATE ALERT ----
    public PriceAlertResponseDTO createAlert(PriceAlertRequestDTO request) {
        Optional<PriceAlert> existing = alertRepository
            .findByUserIdAndMedicineId(request.getUserId(), request.getMedicineId());

        if (existing.isPresent()) {
            return new PriceAlertResponseDTO("Alert already exists for this medicine.");
        }

        PriceAlert alert = new PriceAlert();
        alert.setUserId(request.getUserId());
        alert.setMedicineId(request.getMedicineId());
        alert.setPriceAtSubscribe(request.getPriceAtSubscribe());

        PriceAlert saved = alertRepository.save(alert);
        return new PriceAlertResponseDTO(saved);
    }

    // ---- GET ALERTS BY USER ----
    public List<PriceAlertResponseDTO> getAlertsByUser(Long userId) {
        return alertRepository.findByUserId(userId)
            .stream()
            .map(PriceAlertResponseDTO::new)
            .collect(Collectors.toList());
    }

    // ---- CANCEL BY ALERT ID ----
    public PriceAlertResponseDTO cancelAlert(Long alertId) {
        return alertRepository.findById(alertId).map(alert -> {
            alert.setIsActive(false);
            alertRepository.save(alert);
            return new PriceAlertResponseDTO(alert);
        }).orElse(new PriceAlertResponseDTO("Alert not found."));
    }

    // ---- CHECK AND SEND EMAILS ----
    public void checkAndSendAlerts(Long medicineId, String medicineName, double newLowestPrice) {
        List<PriceAlert> activeAlerts = alertRepository.findByMedicineIdAndIsActiveTrue(medicineId);

        for (PriceAlert alert : activeAlerts) {
            if (newLowestPrice < alert.getPriceAtSubscribe()) {

                // Get user email
                user existingUser = userRepository.findById(alert.getUserId()).orElse(null);

                if (existingUser != null) {
                    // ✅ Actually send email now
                    emailService.sendPriceDropEmail(
                        existingUser.getEmail(),
                        medicineName,
                        alert.getPriceAtSubscribe(),
                        newLowestPrice
                    );
                    System.out.println("✅ Price drop email sent to: " + existingUser.getEmail());
                }

                // Update to new price instead of deactivating
                // So user gets notified on future drops too
                alert.setPriceAtSubscribe(newLowestPrice);
                alertRepository.save(alert);
            }
        }
    }
}