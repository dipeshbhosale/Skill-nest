package com.skillnest.user.controller;

import com.skillnest.user.model.StudentProfile;
import com.skillnest.user.model.TeacherProfile;
import com.skillnest.user.model.User;
import com.skillnest.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ==================== User Endpoints ====================

    @GetMapping("/api/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch users: " + e.getMessage()));
        }
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }
            return ResponseEntity.ok(user);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user: " + e.getMessage()));
        }
    }

    @PutMapping("/api/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody Map<String, Object> updates) {
        try {
            User user = userService.updateUser(userId, updates);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }
            return ResponseEntity.ok(user);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/api/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        try {
            boolean deleted = userService.deleteUser(userId);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete user: " + e.getMessage()));
        }
    }

    // ==================== Teacher Profile Endpoints ====================

    @GetMapping("/api/teachers/{userId}")
    public ResponseEntity<?> getTeacherProfile(@PathVariable String userId) {
        try {
            TeacherProfile profile = userService.getTeacherProfile(userId);
            if (profile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Teacher profile not found"));
            }
            return ResponseEntity.ok(profile);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch teacher profile: " + e.getMessage()));
        }
    }

    @PutMapping("/api/teachers/{userId}")
    public ResponseEntity<?> updateTeacherProfile(@PathVariable String userId, @RequestBody TeacherProfile profile) {
        try {
            TeacherProfile updated = userService.updateTeacherProfile(userId, profile);
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update teacher profile: " + e.getMessage()));
        }
    }

    // ==================== Student Profile Endpoints ====================

    @GetMapping("/api/students/{userId}")
    public ResponseEntity<?> getStudentProfile(@PathVariable String userId) {
        try {
            StudentProfile profile = userService.getStudentProfile(userId);
            if (profile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Student profile not found"));
            }
            return ResponseEntity.ok(profile);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch student profile: " + e.getMessage()));
        }
    }

    @PutMapping("/api/students/{userId}")
    public ResponseEntity<?> updateStudentProfile(@PathVariable String userId, @RequestBody StudentProfile profile) {
        try {
            StudentProfile updated = userService.updateStudentProfile(userId, profile);
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update student profile: " + e.getMessage()));
        }
    }
}
