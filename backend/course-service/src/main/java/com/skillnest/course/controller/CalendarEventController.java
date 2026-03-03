package com.skillnest.course.controller;

import com.skillnest.course.model.CalendarEvent;
import com.skillnest.course.service.CalendarEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/calendar")
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody CalendarEvent event) {
        try {
            CalendarEvent created = calendarEventService.createEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create event: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getEventsByUser(@PathVariable String userId) {
        try {
            List<CalendarEvent> events = calendarEventService.getEventsByUser(userId);
            return ResponseEntity.ok(events);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch events: " + e.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getEventsByCourse(@PathVariable String courseId) {
        try {
            List<CalendarEvent> events = calendarEventService.getEventsByCourse(courseId);
            return ResponseEntity.ok(events);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch events: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable String id) {
        try {
            CalendarEvent event = calendarEventService.getEventById(id);
            if (event == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Event not found"));
            }
            return ResponseEntity.ok(event);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch event: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            CalendarEvent updated = calendarEventService.updateEvent(id, updates);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Event not found"));
            }
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update event: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable String id) {
        try {
            calendarEventService.deleteEvent(id);
            return ResponseEntity.ok(Map.of("message", "Event deleted successfully"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete event: " + e.getMessage()));
        }
    }
}
