package com.skillnest.course.model;

import java.util.HashMap;
import java.util.Map;

public class Enrollment {
    private String id;
    private String courseId;
    private String studentId;
    private long enrolledAt;
    private double progress;
    private String status; // active, completed, dropped

    public Enrollment() {}

    public Enrollment(String id, String courseId, String studentId) {
        this.id = id;
        this.courseId = courseId;
        this.studentId = studentId;
        this.enrolledAt = System.currentTimeMillis();
        this.progress = 0.0;
        this.status = "active";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id != null ? id : "");
        map.put("courseId", courseId != null ? courseId : "");
        map.put("studentId", studentId != null ? studentId : "");
        map.put("enrolledAt", enrolledAt);
        map.put("progress", progress);
        map.put("status", status != null ? status : "active");
        return map;
    }

    public static Enrollment fromMap(Map<String, Object> map) {
        Enrollment enrollment = new Enrollment();
        enrollment.setId((String) map.getOrDefault("id", ""));
        enrollment.setCourseId((String) map.getOrDefault("courseId", ""));
        enrollment.setStudentId((String) map.getOrDefault("studentId", ""));
        enrollment.setEnrolledAt(((Number) map.getOrDefault("enrolledAt", 0L)).longValue());
        enrollment.setProgress(((Number) map.getOrDefault("progress", 0.0)).doubleValue());
        enrollment.setStatus((String) map.getOrDefault("status", "active"));
        return enrollment;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public long getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(long enrolledAt) { this.enrolledAt = enrolledAt; }
    public double getProgress() { return progress; }
    public void setProgress(double progress) { this.progress = progress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
