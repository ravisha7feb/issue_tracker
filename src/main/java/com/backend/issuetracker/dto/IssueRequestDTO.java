package com.backend.issuetracker.dto;

import lombok.Data;

@Data
public class IssueRequestDTO {
    private String title;
    private String description;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private Long reportedBy; // userId
}


/*
{
    "title": "titleTest",
    "description": "desc Test",
    "imageUrl": "imageUrl Test",
    "latitude": 12.40,
    "longitude": 300.30,
    "reportedBy": 123
}
 */