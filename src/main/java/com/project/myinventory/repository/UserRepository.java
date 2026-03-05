package com.project.myinventory.repository;

import com.project.myinventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsernameAndDeletedAtIsNull(String username);
}
