package com.medcompare.backend.repository;

import com.medcompare.backend.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByNameContaining(String name);
        List<Medicine> findByCategory(String category);
            List<Medicine> findByCategoryIgnoreCase(String category);
}
