
package com.medcompare.backend.repository;

import com.medcompare.backend.entity.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    // Get all active alerts for the scheduler
    List<PriceAlert> findByIsActiveTrue();

    // Get all alerts for a specific user
    List<PriceAlert> findByUserId(Long userId);

    // Check if alert already exists (your unique constraint handles DB side,
    // but this lets you give a nice error message)
    Optional<PriceAlert> findByUserIdAndMedicineId(Long userId, Long medicineId);
    List<PriceAlert> findByMedicineIdAndIsActiveTrue(Long medicineId);
}