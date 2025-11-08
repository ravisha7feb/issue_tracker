package com.backend.issuetracker.controller;

import com.backend.issuetracker.dto.CreateIssueRequest;
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
    public ResponseEntity<Issue> createIssue(@RequestBody CreateIssueRequest req) {
        Issue issue = new Issue();
        issue.setTitle(req.title);
        issue.setDescription(req.description);

        // set geometry point if lat/lng provided
        if (req.lat != null && req.lng != null) {
            Point p = GeometryUtils.createPoint(req.lat, req.lng);
            issue.setLocation(p);
        }

        issue.setImageUrl(req.imageUrl);

        // set createdAt using OffsetDateTime (matches entity)
        issue.setCreatedAt(OffsetDateTime.now());

        // set Status using enum from entity
        issue.setStatus(Issue.Status.OPEN);

        // link reporting user if provided
        if (req.reportedByUserId != null) {
            Optional<User> maybeUser = userRepository.findById(req.reportedByUserId);
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
