package com.medcompare.backend.controller;

import com.medcompare.backend.entity.Medicine;
import com.medcompare.backend.repository.MedicineRepository;
import com.medcompare.backend.service.MedicineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;
    @Autowired
    private MedicineRepository medicineRepository;
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
    // @PutMapping("/{id}")
    // public Medicine updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
    //     return medicineService.updateMedicine(id, medicine);
    // }
    @PutMapping("/{id}")
    public Medicine updateMedicine(@PathVariable Long id, @RequestBody Medicine updatedMedicine) {
    Medicine existing = medicineRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Medicine not found"));
    existing.setName(updatedMedicine.getName());
    existing.setPrice(updatedMedicine.getPrice());
    existing.setCategory(updatedMedicine.getCategory());
    existing.setImageUrl(updatedMedicine.getImageUrl());

    existing.setOnemgPrice(updatedMedicine.getOnemgPrice());
    existing.setOnemgUrl(updatedMedicine.getOnemgUrl());


existing.setApolloPrice(updatedMedicine.getApolloPrice());
    existing.setApolloUrl(updatedMedicine.getApolloUrl());

    existing.setPharmeasyPrice(updatedMedicine.getPharmeasyPrice());
    existing.setPharmeasyUrl(updatedMedicine.getPharmeasyUrl());


    return medicineRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return "Medicine deleted successfully";
    }
}