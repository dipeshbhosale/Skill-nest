package com.skillnest.course.controller;

import com.skillnest.course.model.Material;
import com.skillnest.course.service.MaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<?> createMaterial(@RequestBody Material material) {
        try {
            Material created = materialService.createMaterial(material);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload material: " + e.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getMaterialsByCourse(@PathVariable String courseId) {
        try {
            List<Material> materials = materialService.getMaterialsByCourse(courseId);
            return ResponseEntity.ok(materials);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch materials: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable String id) {
        try {
            Material material = materialService.getMaterialById(id);
            if (material == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Material not found"));
            }
            return ResponseEntity.ok(material);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch material: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            Material updated = materialService.updateMaterial(id, updates);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Material not found"));
            }
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update material: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable String id) {
        try {
            materialService.deleteMaterial(id);
            return ResponseEntity.ok(Map.of("message", "Material deleted successfully"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete material: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/download")
    public ResponseEntity<?> incrementDownload(@PathVariable String id) {
        try {
            materialService.incrementDownloads(id);
            return ResponseEntity.ok(Map.of("message", "Download count incremented"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update download count: " + e.getMessage()));
        }
    }
}
