package com.skillnest.assignment.model;

import java.util.HashMap;
import java.util.Map;

public class Assignment {

    private String id;
    private String courseId;
    private String title;
    private String description;
    private String dueDate;
    private int totalMarks;
    private String attachmentUrl;
    private String status; // active, closed
    private int submissionCount;
    private String createdBy;
    private String createdAt;
    private String updatedAt;

    public Assignment() {
    }

    public Assignment(String id, String courseId, String title, String description, String dueDate,
                      int totalMarks, String attachmentUrl, String status, int submissionCount,
                      String createdBy, String createdAt, String updatedAt) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.totalMarks = totalMarks;
        this.attachmentUrl = attachmentUrl;
        this.status = status;
        this.submissionCount = submissionCount;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("courseId", courseId);
        map.put("title", title);
        map.put("description", description);
        map.put("dueDate", dueDate);
        map.put("totalMarks", totalMarks);
        map.put("attachmentUrl", attachmentUrl);
        map.put("status", status);
        map.put("submissionCount", submissionCount);
        map.put("createdBy", createdBy);
        map.put("createdAt", createdAt);
        map.put("updatedAt", updatedAt);
        return map;
    }

    public static Assignment fromMap(Map<String, Object> map) {
        Assignment assignment = new Assignment();
        assignment.setId((String) map.get("id"));
        assignment.setCourseId((String) map.get("courseId"));
        assignment.setTitle((String) map.get("title"));
        assignment.setDescription((String) map.get("description"));
        assignment.setDueDate((String) map.get("dueDate"));
        assignment.setTotalMarks(map.get("totalMarks") != null ? ((Number) map.get("totalMarks")).intValue() : 0);
        assignment.setAttachmentUrl((String) map.get("attachmentUrl"));
        assignment.setStatus((String) map.get("status"));
        assignment.setSubmissionCount(map.get("submissionCount") != null ? ((Number) map.get("submissionCount")).intValue() : 0);
        assignment.setCreatedBy((String) map.get("createdBy"));
        assignment.setCreatedAt((String) map.get("createdAt"));
        assignment.setUpdatedAt((String) map.get("updatedAt"));
        return assignment;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSubmissionCount() {
        return submissionCount;
    }

    public void setSubmissionCount(int submissionCount) {
        this.submissionCount = submissionCount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
