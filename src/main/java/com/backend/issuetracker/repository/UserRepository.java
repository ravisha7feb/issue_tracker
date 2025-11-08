package com.backend.issuetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.backend.issuetracker.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
