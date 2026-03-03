package com.skillnest.course.controller;

import com.skillnest.course.model.Reschedule;
import com.skillnest.course.service.RescheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/reschedules")
public class RescheduleController {

    private final RescheduleService rescheduleService;

    public RescheduleController(RescheduleService rescheduleService) {
        this.rescheduleService = rescheduleService;
    }

    @PostMapping
    public ResponseEntity<?> createReschedule(@RequestBody Reschedule reschedule) {
        try {
            Reschedule created = rescheduleService.createReschedule(reschedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create reschedule request: " + e.getMessage()));
        }
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getReschedulesByClass(@PathVariable String classId) {
        try {
            List<Reschedule> reschedules = rescheduleService.getReschedulesByClass(classId);
            return ResponseEntity.ok(reschedules);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch reschedules: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRescheduleById(@PathVariable String id) {
        try {
            Reschedule reschedule = rescheduleService.getRescheduleById(id);
            if (reschedule == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Reschedule not found"));
            }
            return ResponseEntity.ok(reschedule);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch reschedule: " + e.getMessage()));
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingReschedules() {
        try {
            List<Reschedule> reschedules = rescheduleService.getPendingReschedules();
            return ResponseEntity.ok(reschedules);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch pending reschedules: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateRescheduleStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            String status = body.get("status");
            if (status == null || (!status.equals("approved") && !status.equals("rejected"))) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Status must be 'approved' or 'rejected'"));
            }
            Reschedule updated = rescheduleService.updateRescheduleStatus(id, status);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Reschedule not found"));
            }
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update reschedule: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReschedule(@PathVariable String id) {
        try {
            rescheduleService.deleteReschedule(id);
            return ResponseEntity.ok(Map.of("message", "Reschedule deleted successfully"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete reschedule: " + e.getMessage()));
        }
    }
}
