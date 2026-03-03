package com.skillnest.course.controller;

import com.skillnest.course.model.Course;
import com.skillnest.course.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course, Authentication authentication) {
        try {
            String userId = (String) authentication.getPrincipal();
            course.setTeacherId(userId);
            Course created = courseService.createCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create course: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch courses: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable String id) {
        try {
            Course course = courseService.getCourseById(id);
            if (course == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Course not found"));
            }
            return ResponseEntity.ok(course);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch course: " + e.getMessage()));
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getCoursesByTeacher(@PathVariable String teacherId) {
        try {
            List<Course> courses = courseService.getCoursesByTeacher(teacherId);
            return ResponseEntity.ok(courses);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch courses: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            Course updated = courseService.updateCourse(id, updates);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Course not found"));
            }
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update course: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok(Map.of("message", "Course deleted successfully"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete course: " + e.getMessage()));
        }
    }
}
