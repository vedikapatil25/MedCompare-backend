package com.medcompare.backend.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.medcompare.backend.entity.PriceAlert;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    // Find all active subscribers whose subscribed price is higher than new price
    @Query("SELECT pa FROM PriceAlert pa WHERE pa.medicineId = :medicineId " +
           "AND pa.isActive = true AND pa.priceAtSubscribe > :newPrice")
    List<PriceAlert> findSubscribersToNotify(
        @Param("medicineId") Long medicineId,
        @Param("newPrice") Double newPrice
    );

    // Check if user already subscribed
    Optional<PriceAlert> findByUserIdAndMedicineId(Long userId, Long medicineId);
}

