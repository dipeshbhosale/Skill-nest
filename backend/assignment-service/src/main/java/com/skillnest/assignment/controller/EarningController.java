package com.skillnest.assignment.controller;

import com.skillnest.assignment.model.Earning;
import com.skillnest.assignment.service.EarningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/earnings")
public class EarningController {

    private final EarningService earningService;

    public EarningController(EarningService earningService) {
        this.earningService = earningService;
    }

    @PostMapping
    public ResponseEntity<?> createEarning(@RequestBody Earning earning) {
        try {
            Earning created = earningService.createEarning(earning);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to create earning: " + e.getMessage()));
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getEarningsByTeacher(@PathVariable String teacherId) {
        try {
            List<Earning> earnings = earningService.getEarningsByTeacher(teacherId);
            return ResponseEntity.ok(earnings);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch earnings: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEarningById(@PathVariable String id) {
        try {
            Earning earning = earningService.getEarningById(id);
            if (earning == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorResponse("Earning not found"));
            }
            return ResponseEntity.ok(earning);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch earning: " + e.getMessage()));
        }
    }

    @GetMapping("/teacher/{teacherId}/total")
    public ResponseEntity<?> getTotalEarnings(@PathVariable String teacherId) {
        try {
            double total = earningService.getTotalEarnings(teacherId);
            Map<String, Object> response = new HashMap<>();
            response.put("teacherId", teacherId);
            response.put("totalEarnings", total);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to calculate total earnings: " + e.getMessage()));
        }
    }

    @GetMapping("/teacher/{teacherId}/month/{yearMonth}")
    public ResponseEntity<?> getEarningsByMonth(@PathVariable String teacherId, @PathVariable String yearMonth) {
        try {
            List<Earning> earnings = earningService.getEarningsByMonth(teacherId, yearMonth);
            return ResponseEntity.ok(earnings);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to fetch earnings by month: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateEarningStatus(@PathVariable String id, @RequestBody Map<String, String> statusData) {
        try {
            String status = statusData.get("status");
            if (status == null || status.isEmpty()) {
                return ResponseEntity.badRequest().body(errorResponse("Status is required"));
            }
            Earning updated = earningService.updateEarningStatus(id, status);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorResponse("Earning not found"));
            }
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Failed to update earning status: " + e.getMessage()));
        }
    }

    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
