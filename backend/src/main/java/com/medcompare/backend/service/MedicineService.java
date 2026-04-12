package com.medcompare.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medcompare.backend.entity.Medicine;
import com.medcompare.backend.repository.MedicineRepository;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;

    // Constructor Injection
    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    // GET all medicines
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    // GET by ID
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
    }

    // CREATE
    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    // UPDATE
    public Medicine updateMedicine(Long id, Medicine updatedMedicine) {
        Medicine existing = getMedicineById(id);
        existing.setName(updatedMedicine.getName());
        existing.setPrice(updatedMedicine.getPrice());
        return medicineRepository.save(existing);
    }

    // DELETE
    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }
    public List<Medicine> searchMedicine(String name) {
    return medicineRepository.findByNameContaining(name);
    }
    public List<Medicine> getByCategory(String category) {
    return medicineRepository.findByCategoryIgnoreCase(category);
}
}