package com.backend.issuetracker.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.backend.issuetracker.model.TestEntity;
import com.backend.issuetracker.repository.TestRepository;

@Service
public class TestService {
    private final TestRepository repository;

    public TestService(TestRepository repository) {
        this.repository = repository;
    }

    public TestEntity saveMessage(String message) {
        return repository.save(new TestEntity(message));
    }

    public List<TestEntity> getAll() {
        return repository.findAll();
    }
}
