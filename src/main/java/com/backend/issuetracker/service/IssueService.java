package com.backend.issuetracker.service;

import com.backend.issuetracker.model.Issue;
import com.backend.issuetracker.model.User;
import com.backend.issuetracker.repository.IssueRepository;
import com.backend.issuetracker.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public IssueService(IssueRepository issueRepository, UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new Issue.
     */
    @Transactional
    public Issue createIssue(Issue issue, Long userId) {
        // Attach user
        Optional<User> reporter = userRepository.findById(userId);
        reporter.ifPresent(issue::setReportedBy);

        // Set defaults
        issue.setCreatedAt(OffsetDateTime.now());
        issue.setIssueStatus(Issue.IssueStatus.OPEN);

        return issueRepository.save(issue);
    }

    /**
     * Get all issues.
     */
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    /**
     * Get issue by ID.
     */
    public Optional<Issue> getIssueById(Long id) {
        return issueRepository.findById(id);
    }

    /**
     * Update issue status (OPEN → IN_PROGRESS → RESOLVED → CLOSED)
     */
    @Transactional
    public Optional<Issue> updateStatus(Long id, Issue.IssueStatus status) {
        Optional<Issue> opt = issueRepository.findById(id);

        if (opt.isPresent()) {
            Issue issue = opt.get();
            issue.setIssueStatus(status);
            return Optional.of(issueRepository.save(issue));
        }
        return Optional.empty();
    }

    /**
     * Delete an issue.
     */
    public boolean deleteIssue(Long id) {
        if (!issueRepository.existsById(id)) return false;
        issueRepository.deleteById(id);
        return true;
    }
}
