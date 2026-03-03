package com.skillnest.course.model;

import java.util.HashMap;
import java.util.Map;

public class Reschedule {
    private String id;
    private String classId;
    private String originalDate;
    private String newDate;
    private String reason;
    private String status; // pending, approved, rejected
    private String requestedBy;
    private long createdAt;

    public Reschedule() {}

    public Reschedule(String id, String classId, String originalDate, String newDate,
                      String reason, String requestedBy) {
        this.id = id;
        this.classId = classId;
        this.originalDate = originalDate;
        this.newDate = newDate;
        this.reason = reason;
        this.status = "pending";
        this.requestedBy = requestedBy;
        this.createdAt = System.currentTimeMillis();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id != null ? id : "");
        map.put("classId", classId != null ? classId : "");
        map.put("originalDate", originalDate != null ? originalDate : "");
        map.put("newDate", newDate != null ? newDate : "");
        map.put("reason", reason != null ? reason : "");
        map.put("status", status != null ? status : "pending");
        map.put("requestedBy", requestedBy != null ? requestedBy : "");
        map.put("createdAt", createdAt);
        return map;
    }

    public static Reschedule fromMap(Map<String, Object> map) {
        Reschedule reschedule = new Reschedule();
        reschedule.setId((String) map.getOrDefault("id", ""));
        reschedule.setClassId((String) map.getOrDefault("classId", ""));
        reschedule.setOriginalDate((String) map.getOrDefault("originalDate", ""));
        reschedule.setNewDate((String) map.getOrDefault("newDate", ""));
        reschedule.setReason((String) map.getOrDefault("reason", ""));
        reschedule.setStatus((String) map.getOrDefault("status", "pending"));
        reschedule.setRequestedBy((String) map.getOrDefault("requestedBy", ""));
        reschedule.setCreatedAt(((Number) map.getOrDefault("createdAt", 0L)).longValue());
        return reschedule;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
    public String getOriginalDate() { return originalDate; }
    public void setOriginalDate(String originalDate) { this.originalDate = originalDate; }
    public String getNewDate() { return newDate; }
    public void setNewDate(String newDate) { this.newDate = newDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
