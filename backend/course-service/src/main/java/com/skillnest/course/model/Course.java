package com.skillnest.course.model;

import java.util.HashMap;
import java.util.Map;

public class Course {
    private String id;
    private String title;
    private String description;
    private String category;
    private String teacherId;
    private String teacherName;
    private String thumbnail;
    private int totalClasses;
    private int enrolledStudents;
    private String status; // active, inactive, draft
    private long createdAt;
    private long updatedAt;

    public Course() {}

    public Course(String id, String title, String description, String category,
                  String teacherId, String teacherName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.thumbnail = "";
        this.totalClasses = 0;
        this.enrolledStudents = 0;
        this.status = "active";
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id != null ? id : "");
        map.put("title", title != null ? title : "");
        map.put("description", description != null ? description : "");
        map.put("category", category != null ? category : "");
        map.put("teacherId", teacherId != null ? teacherId : "");
        map.put("teacherName", teacherName != null ? teacherName : "");
        map.put("thumbnail", thumbnail != null ? thumbnail : "");
        map.put("totalClasses", totalClasses);
        map.put("enrolledStudents", enrolledStudents);
        map.put("status", status != null ? status : "active");
        map.put("createdAt", createdAt);
        map.put("updatedAt", updatedAt);
        return map;
    }

    public static Course fromMap(Map<String, Object> map) {
        Course course = new Course();
        course.setId((String) map.getOrDefault("id", ""));
        course.setTitle((String) map.getOrDefault("title", ""));
        course.setDescription((String) map.getOrDefault("description", ""));
        course.setCategory((String) map.getOrDefault("category", ""));
        course.setTeacherId((String) map.getOrDefault("teacherId", ""));
        course.setTeacherName((String) map.getOrDefault("teacherName", ""));
        course.setThumbnail((String) map.getOrDefault("thumbnail", ""));
        course.setTotalClasses(((Number) map.getOrDefault("totalClasses", 0)).intValue());
        course.setEnrolledStudents(((Number) map.getOrDefault("enrolledStudents", 0)).intValue());
        course.setStatus((String) map.getOrDefault("status", "active"));
        course.setCreatedAt(((Number) map.getOrDefault("createdAt", 0L)).longValue());
        course.setUpdatedAt(((Number) map.getOrDefault("updatedAt", 0L)).longValue());
        return course;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public int getTotalClasses() { return totalClasses; }
    public void setTotalClasses(int totalClasses) { this.totalClasses = totalClasses; }
    public int getEnrolledStudents() { return enrolledStudents; }
    public void setEnrolledStudents(int enrolledStudents) { this.enrolledStudents = enrolledStudents; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
