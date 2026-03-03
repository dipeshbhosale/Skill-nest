package com.skillnest.course.controller;

import com.skillnest.course.model.Enrollment;
import com.skillnest.course.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<?> enrollStudent(@RequestBody Enrollment enrollment) {
        try {
            Enrollment created = enrollmentService.enrollStudent(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to enroll student: " + e.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getEnrollmentsByCourse(@PathVariable String courseId) {
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
            return ResponseEntity.ok(enrollments);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch enrollments: " + e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getEnrollmentsByStudent(@PathVariable String studentId) {
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
            return ResponseEntity.ok(enrollments);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch enrollments: " + e.getMessage()));
        }
    }

    @DeleteMapping("/course/{courseId}/student/{studentId}")
    public ResponseEntity<?> unenrollStudent(@PathVariable String courseId, @PathVariable String studentId) {
        try {
            enrollmentService.unenrollStudent(courseId, studentId);
            return ResponseEntity.ok(Map.of("message", "Student unenrolled successfully"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to unenroll student: " + e.getMessage()));
        }
    }
}
