package com.skillnest.course.controller;

import com.skillnest.course.model.ClassSession;
import com.skillnest.course.service.ClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping
    public ResponseEntity<?> createClass(@RequestBody ClassSession classSession) {
        try {
            ClassSession created = classService.createClass(classSession);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create class: " + e.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getClassesByCourse(@PathVariable String courseId) {
        try {
            List<ClassSession> classes = classService.getClassesByCourse(courseId);
            return ResponseEntity.ok(classes);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch classes: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClassById(@PathVariable String id) {
        try {
            ClassSession classSession = classService.getClassById(id);
            if (classSession == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Class not found"));
            }
            return ResponseEntity.ok(classSession);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch class: " + e.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}/upcoming")
    public ResponseEntity<?> getUpcomingClasses(@PathVariable String courseId) {
        try {
            List<ClassSession> classes = classService.getUpcomingClasses(courseId);
            return ResponseEntity.ok(classes);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch upcoming classes: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClass(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            ClassSession updated = classService.updateClass(id, updates);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Class not found"));
            }
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update class: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable String id) {
        try {
            classService.deleteClass(id);
            return ResponseEntity.ok(Map.of("message", "Class deleted successfully"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete class: " + e.getMessage()));
        }
    }
}
