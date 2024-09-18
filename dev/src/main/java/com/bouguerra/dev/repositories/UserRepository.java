package com.bouguerra.dev.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bouguerra.dev.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    
     boolean existsByEmail(String email);

     boolean existsByUsername(String username);
      User findUserByEmail(String email);
}