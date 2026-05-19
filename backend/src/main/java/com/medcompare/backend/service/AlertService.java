package com.medcompare.backend.service;

import java.util.List;
import com.medcompare.backend.entity.AlertLog;
import com.medcompare.backend.entity.PriceAlert;
import com.medcompare.backend.model.user;                    // your User model
import com.medcompare.backend.repository.AlertLogRepository;
import com.medcompare.backend.repository.PriceAlertRepository;
import com.medcompare.backend.repository.UserRepository;     // only this one, not entity.UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    @Autowired
    private PriceAlertRepository alertRepo;

    @Autowired
    private AlertLogRepository logRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    public void checkAndSendAlerts(Long medicineId, String medicineName, Double newLowestPrice) {

        List<PriceAlert> subscribers = alertRepo.findSubscribersToNotify(medicineId, newLowestPrice);

        if (subscribers.isEmpty()) return;

        for (PriceAlert alert : subscribers) {

            // renamed variable to "u" to avoid conflict with class name "user"
            user u = userRepo.findById(alert.getUserId()).orElse(null);
            if (u == null) continue;

            emailService.sendPriceDropEmail(
                u.getEmail(),
                u.getName(),
                medicineName,
                alert.getPriceAtSubscribe(),
                newLowestPrice
            );

            AlertLog log = new AlertLog();
            log.setUserId(alert.getUserId());
            log.setMedicineId(medicineId);
            log.setOldPrice(alert.getPriceAtSubscribe());
            log.setNewPrice(newLowestPrice);
            logRepo.save(log);

            alert.setPriceAtSubscribe(newLowestPrice);
            alertRepo.save(alert);
        }
    }
}