package com.backend.issuetracker.repository;

import com.backend.issuetracker.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}