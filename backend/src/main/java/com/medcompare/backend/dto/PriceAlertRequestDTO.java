// PriceAlertRequestDTO.java  — what frontend sends
package com.medcompare.backend.dto;

public class PriceAlertRequestDTO {
    private Long userId;
    private Long medicineId;
    private Double priceAtSubscribe;

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }

    public Double getPriceAtSubscribe() { return priceAtSubscribe; }
    public void setPriceAtSubscribe(Double p) { this.priceAtSubscribe = p; }
}