package com.backend.issuetracker.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class IssueResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String imageUrl;
    private Instant createdAt;
    private double latitude;
    private double longitude;
    private String reporterName;
}
