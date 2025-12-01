package com.backend.issuetracker.model;

import jakarta.persistence.*;
import lombok.Data;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "issue")
public class Issue {

    public enum IssueStatus { OPEN, IN_PROGRESS, RESOLVED, CLOSED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "text")
    private String description;

    /**
     * PostGIS geometry column, SRID 4326 (WGS84 lat/lon)
     * Using columnDefinition ensures Hibernate creates a geometry column.
     */
    @JsonIgnore
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus = IssueStatus.OPEN;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Issue() {}

    // getters & setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public IssueStatus getIssueStatus() { return issueStatus; }
    public void setIssueStatus(IssueStatus issueStatus) { this.issueStatus = issueStatus; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public User getReportedBy() { return reportedBy; }
    public void setReportedBy(User reportedBy) { this.reportedBy = reportedBy; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    // Expose location as a readable WKT string in JSON:
    @JsonProperty("location")
    public String getLocationAsText() {
        return location != null ? location.toText() : null;
    }
}
