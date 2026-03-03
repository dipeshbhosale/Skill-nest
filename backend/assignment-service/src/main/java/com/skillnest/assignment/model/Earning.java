package com.skillnest.assignment.model;

import java.util.HashMap;
import java.util.Map;

public class Earning {

    private String id;
    private String teacherId;
    private String courseId;
    private String classId;
    private double amount;
    private String date;
    private String status; // pending, paid
    private String description;
    private String createdAt;

    public Earning() {
    }

    public Earning(String id, String teacherId, String courseId, String classId, double amount,
                   String date, String status, String description, String createdAt) {
        this.id = id;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.classId = classId;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("teacherId", teacherId);
        map.put("courseId", courseId);
        map.put("classId", classId);
        map.put("amount", amount);
        map.put("date", date);
        map.put("status", status);
        map.put("description", description);
        map.put("createdAt", createdAt);
        return map;
    }

    public static Earning fromMap(Map<String, Object> map) {
        Earning earning = new Earning();
        earning.setId((String) map.get("id"));
        earning.setTeacherId((String) map.get("teacherId"));
        earning.setCourseId((String) map.get("courseId"));
        earning.setClassId((String) map.get("classId"));
        earning.setAmount(map.get("amount") != null ? ((Number) map.get("amount")).doubleValue() : 0.0);
        earning.setDate((String) map.get("date"));
        earning.setStatus((String) map.get("status"));
        earning.setDescription((String) map.get("description"));
        earning.setCreatedAt((String) map.get("createdAt"));
        return earning;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
