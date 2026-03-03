package com.skillnest.course.controller;

import com.skillnest.course.model.Attendance;
import com.skillnest.course.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<?> markAttendance(@RequestBody Attendance attendance) {
        try {
            Attendance marked = attendanceService.markAttendance(attendance);
            return ResponseEntity.status(HttpStatus.CREATED).body(marked);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to mark attendance: " + e.getMessage()));
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> markBulkAttendance(@RequestBody List<Attendance> attendanceList) {
        try {
            List<Attendance> results = attendanceService.markBulkAttendance(attendanceList);
            return ResponseEntity.status(HttpStatus.CREATED).body(results);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to mark bulk attendance: " + e.getMessage()));
        }
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getAttendanceByClass(@PathVariable String classId) {
        try {
            List<Attendance> attendanceList = attendanceService.getAttendanceByClass(classId);
            return ResponseEntity.ok(attendanceList);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch attendance: " + e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getAttendanceByStudent(@PathVariable String studentId) {
        try {
            List<Attendance> attendanceList = attendanceService.getAttendanceByStudent(studentId);
            return ResponseEntity.ok(attendanceList);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch attendance: " + e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<?> getAttendanceByStudentAndCourse(@PathVariable String studentId, @PathVariable String courseId) {
        try {
            List<Attendance> attendanceList = attendanceService.getAttendanceByStudentAndCourse(studentId, courseId);
            return ResponseEntity.ok(attendanceList);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch attendance: " + e.getMessage()));
        }
    }
}
