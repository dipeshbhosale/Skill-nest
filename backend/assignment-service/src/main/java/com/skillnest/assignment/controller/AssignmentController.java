package com.skillnest.assignment.controller;

import com.skillnest.assignment.model.Assignment;
import com.skillnest.assignment.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<?> createAssignment(@RequestBody Assignment assignment) {
        try {
            Assignment created = assignmentService.createAssignment(assignment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to create assignment: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAssignments() {
        try {
            List<Assignment> assignments = assignmentService.getAllAssignments();
            return ResponseEntity.ok(assignments);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch assignments: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable String id) {
        try {
            Assignment assignment = assignmentService.getAssignmentById(id);
            if (assignment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorResponse("Assignment not found"));
            }
            return ResponseEntity.ok(assignment);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch assignment: " + e.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getAssignmentsByCourse(@PathVariable String courseId) {
        try {
            List<Assignment> assignments = assignmentService.getAssignmentsByCourse(courseId);
            return ResponseEntity.ok(assignments);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch assignments: " + e.getMessage()));
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getAssignmentsByTeacher(@PathVariable String teacherId) {
        try {
            List<Assignment> assignments = assignmentService.getAssignmentsByTeacher(teacherId);
            return ResponseEntity.ok(assignments);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch assignments: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable String id, @RequestBody Assignment assignment) {
        try {
            Assignment updated = assignmentService.updateAssignment(id, assignment);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorResponse("Assignment not found"));
            }
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to update assignment: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable String id) {
        try {
            boolean deleted = assignmentService.deleteAssignment(id);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorResponse("Assignment not found"));
            }
            Map<String, String> response = new HashMap<>();
            response.put("message", "Assignment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to delete assignment: " + e.getMessage()));
        }
    }

    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
