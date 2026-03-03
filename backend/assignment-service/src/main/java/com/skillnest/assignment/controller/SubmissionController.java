package com.skillnest.assignment.controller;

import com.skillnest.assignment.model.Submission;
import com.skillnest.assignment.service.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<?> createSubmission(@RequestBody Submission submission) {
        try {
            Submission created = submissionService.createSubmission(submission);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to create submission: " + e.getMessage()));
        }
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<?> getSubmissionsByAssignment(@PathVariable String assignmentId) {
        try {
            List<Submission> submissions = submissionService.getSubmissionsByAssignment(assignmentId);
            return ResponseEntity.ok(submissions);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch submissions: " + e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getSubmissionsByStudent(@PathVariable String studentId) {
        try {
            List<Submission> submissions = submissionService.getSubmissionsByStudent(studentId);
            return ResponseEntity.ok(submissions);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch submissions: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubmissionById(@PathVariable String id) {
        try {
            Submission submission = submissionService.getSubmissionById(id);
            if (submission == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorResponse("Submission not found"));
            }
            return ResponseEntity.ok(submission);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch submission: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<?> gradeSubmission(@PathVariable String id, @RequestBody Map<String, Object> gradeData) {
        try {
            Submission graded = submissionService.gradeSubmission(id, gradeData);
            if (graded == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorResponse("Submission not found"));
            }
            return ResponseEntity.ok(graded);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to grade submission: " + e.getMessage()));
        }
    }

    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
