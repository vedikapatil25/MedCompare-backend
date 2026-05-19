package com.medcompare.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.medcompare.backend.entity.AlertLog;

@Repository
public interface AlertLogRepository extends JpaRepository<AlertLog, Long> {

}