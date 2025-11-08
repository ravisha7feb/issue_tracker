package com.backend.issuetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.issuetracker.model.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    // we'll add geo queries later (using @Query with ST_DWithin etc.)
}
