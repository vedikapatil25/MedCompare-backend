
package com.medcompare.backend.dto;
import com.medcompare.backend.entity.PriceAlert;
import java.time.LocalDateTime;

public class PriceAlertResponseDTO {
    private Long id;
    private Long medicineId;
    private Double priceAtSubscribe;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private String message;

    // Constructor for success
    public PriceAlertResponseDTO(PriceAlert alert) {
        this.id = alert.getId();
        this.medicineId = alert.getMedicineId();
        this.priceAtSubscribe = alert.getPriceAtSubscribe();
        this.isActive = alert.getIsActive();
        this.createdAt = alert.getCreatedAt();
    }

    // Constructor for error/info messages
    public PriceAlertResponseDTO(String message) {
        this.message = message;
    }

    // Getters
    public Long getId() { return id; }
    public Long getMedicineId() { return medicineId; }
    public Double getPriceAtSubscribe() { return priceAtSubscribe; }
    public Boolean getIsActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getMessage() { return message; }
}