package com.backend.issuetracker.controller;

import com.backend.issuetracker.dto.IssueRequestDTO;
import com.backend.issuetracker.model.Issue;
import com.backend.issuetracker.model.User;
import com.backend.issuetracker.repository.IssueRepository;
import com.backend.issuetracker.repository.UserRepository;
import com.backend.issuetracker.util.GeometryUtils;
import org.locationtech.jts.geom.Point;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Issue> createIssue(
        @RequestPart("data") IssueRequestDTO req,
        @RequestPart(value = "file", required = false) MultipartFile file) 
    {
        Issue issue = new Issue();
        issue.setTitle(req.getTitle());
        issue.setDescription(req.getDescription());

        // set geometry point if latitude/longitude provided
        Point p = GeometryUtils.createPoint(req.getLatitude(), req.getLongitude());
        issue.setLocation(p);

        // handle image upload
        String imageUrl = null;
        if (file != null && !file.isEmpty()) {
            System.out.println("Received file: " + file.getOriginalFilename());
            try {
                // Use absolute path: project root or user home
                String uploadDirPath = System.getProperty("user.dir") + File.separator + "uploads";
                File uploadDir = new File(uploadDirPath);
                if (!uploadDir.exists()) {
                    boolean created = uploadDir.mkdirs();
                    System.out.println("Upload dir created: " + created + " at " + uploadDir.getAbsolutePath());
                }

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                File destination = new File(uploadDir, fileName);

                file.transferTo(destination);
                System.out.println("File saved to: " + destination.getAbsolutePath());
                imageUrl = "/uploads/" + fileName;

            } catch (Exception e) {
                System.err.println("Error saving file: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(500).body(null);
            }
        }

        issue.setImageUrl(imageUrl);  // Set uploaded file URL (or null)
        issue.setCreatedAt(OffsetDateTime.now());
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
