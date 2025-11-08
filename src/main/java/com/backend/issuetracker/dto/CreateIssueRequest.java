package com.backend.issuetracker.dto;

public class CreateIssueRequest {
    public String title;
    public String description;
    public Double lat;    // nullable if you allow missing
    public Double lng;    // nullable if you allow missing
    public Long reportedByUserId;
    public String imageUrl; // optional
}
