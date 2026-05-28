package com.medcompare.backend.controller;
import java.util.Map;
import com.medcompare.backend.entity.Medicine;
import com.medcompare.backend.repository.MedicineRepository;
import com.medcompare.backend.service.MedicineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.medcompare.backend.service.PriceAlertService;   
import java.util.stream.Stream;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private PriceAlertService alertService;             


    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping
    public List<Medicine> getAllMedicines() {
        return medicineService.getAllMedicines();
    }
    @GetMapping("/category/{category}")
    public List<Medicine> getByCategory(@PathVariable String category) {
    return medicineService.getByCategory(category);
    }

    @PostMapping
    public Medicine addMedicine(@RequestBody Medicine medicine) {
        return medicineService.saveMedicine(medicine);
    }

    @GetMapping("/{id}")
    public Medicine getMedicineById(@PathVariable Long id) {
        return medicineService.getMedicineById(id);
    }
  @GetMapping("/search")
public List<Medicine> search(@RequestParam String name) {
    return medicineService.searchMedicine(name);
}
   
    @PutMapping("/{id}")
    public Medicine updateMedicine(@PathVariable Long id, @RequestBody Medicine updatedMedicine) {
    Medicine existing = medicineRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Medicine not found"));
    existing.setName(updatedMedicine.getName());
    existing.setCategory(updatedMedicine.getCategory());
    existing.setImageUrl(updatedMedicine.getImageUrl());

    existing.setOnemgPrice(updatedMedicine.getOnemgPrice());
    existing.setOnemgUrl(updatedMedicine.getOnemgUrl());


    existing.setApolloPrice(updatedMedicine.getApolloPrice());
    existing.setApolloUrl(updatedMedicine.getApolloUrl());

    existing.setPharmeasyPrice(updatedMedicine.getPharmeasyPrice());
    existing.setPharmeasyUrl(updatedMedicine.getPharmeasyUrl());


            Medicine saved = medicineRepository.save(existing);

        double newLowest = Stream.of(
                saved.getApolloPrice(),
                saved.getOnemgPrice(),
                saved.getPharmeasyPrice()
            )
            .filter(p -> p != null && p > 0)
            .mapToDouble(Double::doubleValue)
            .min().orElse(0.0);
        alertService.checkAndSendAlerts(id, saved.getName(), newLowest);

        return saved;

    }

    @DeleteMapping("/{id}")
    public String deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return "Medicine deleted successfully";
    }
    

    @PutMapping("/{id}/prices")
    public ResponseEntity<Medicine> updatePrices(
    @PathVariable Long id,
    @RequestBody Map<String, Double> prices) {

    Medicine med = medicineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Medicine not found"));

    med.setOnemgPrice(prices.get("onemgPrice"));
    med.setApolloPrice(prices.get("apolloPrice"));
    med.setPharmeasyPrice(prices.get("pharmeasyPrice"));
    med.setPriceUpdatedAt(LocalDateTime.now());

    Medicine saved = medicineRepository.save(med);

    // ✅ Add this — trigger price drop alerts
    double newLowest = Stream.of(
            saved.getApolloPrice(),
            saved.getOnemgPrice(),
            saved.getPharmeasyPrice()
        )
        .filter(p -> p != null && p > 0)
        .mapToDouble(Double::doubleValue)
        .min().orElse(0.0);

    alertService.checkAndSendAlerts(id, saved.getName(), newLowest);

    return ResponseEntity.ok(saved);
}
}