package com.backend.issuetracker.controller;

import com.backend.issuetracker.dto.IssueRequestDTO;
import com.backend.issuetracker.model.Issue;
import com.backend.issuetracker.model.User;
import com.backend.issuetracker.repository.IssueRepository;
import com.backend.issuetracker.repository.UserRepository;
import com.backend.issuetracker.util.GeometryUtils;
import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*")
public class IssueController {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public IssueController(IssueRepository issueRepository, UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Issue> createIssue(@RequestBody IssueRequestDTO req) {
        Issue issue = new Issue();
        issue.setTitle(req.getTitle());
        issue.setDescription(req.getDescription());

        // set geometry point if latitude/longitude provided
        Point p = GeometryUtils.createPoint(req.getLatitude(), req.getLongitude());
        issue.setLocation(p);

        issue.setImageUrl(req.getImageUrl());

        // set createdAt using OffsetDateTime (matches entity)
        issue.setCreatedAt(OffsetDateTime.now());

        // set IssueStatus using enum from entity
        issue.setIssueStatus(Issue.IssueStatus.OPEN);

        // link reporting user if provided
        if (req.getReportedBy() != null) {
            Optional<User> maybeUser = userRepository.findById(req.getReportedBy());
            maybeUser.ifPresent(issue::setReportedBy);
        }

        Issue saved = issueRepository.save(issue);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Issue> getAll() {
        return issueRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> getById(@PathVariable Long id) {
        return issueRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
