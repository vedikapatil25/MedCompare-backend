package com.medcompare.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medcompare.backend.model.user;

public interface UserRepository extends JpaRepository<user, Long> {

    user findByEmail(String email);   // IMPORTANT for login
}