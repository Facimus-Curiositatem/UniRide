package com.uniride.backend.repository;

import com.uniride.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
    
    @Query("SELECT AVG(u.rating) FROM User u")
    Double getAverageRating();
}