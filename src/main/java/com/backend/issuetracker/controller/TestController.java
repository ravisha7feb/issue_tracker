package com.backend.issuetracker.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.backend.issuetracker.model.TestEntity;
import com.backend.issuetracker.service.TestService;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final TestService service;

    public TestController(TestService service) {
        this.service = service;
    }

    @PostMapping
    public TestEntity saveMessage(@RequestParam String message) {
        return service.saveMessage(message);
    }

    @GetMapping
    public List<TestEntity> getAllMessages() {
        return service.getAll();
    }
}
