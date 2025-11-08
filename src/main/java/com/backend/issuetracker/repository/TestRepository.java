package com.backend.issuetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.issuetracker.model.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
