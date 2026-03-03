package com.skillnest.assignment.model;

import java.util.HashMap;
import java.util.Map;

public class Submission {

    private String id;
    private String assignmentId;
    private String studentId;
    private String studentName;
    private String fileUrl;
    private String submittedAt;
    private Double grade;
    private String feedback;
    private String status; // submitted, graded, late
    private String gradedBy;
    private String gradedAt;

    public Submission() {
    }

    public Submission(String id, String assignmentId, String studentId, String studentName,
                      String fileUrl, String submittedAt, Double grade, String feedback,
                      String status, String gradedBy, String gradedAt) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.fileUrl = fileUrl;
        this.submittedAt = submittedAt;
        this.grade = grade;
        this.feedback = feedback;
        this.status = status;
        this.gradedBy = gradedBy;
        this.gradedAt = gradedAt;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("assignmentId", assignmentId);
        map.put("studentId", studentId);
        map.put("studentName", studentName);
        map.put("fileUrl", fileUrl);
        map.put("submittedAt", submittedAt);
        map.put("grade", grade);
        map.put("feedback", feedback);
        map.put("status", status);
        map.put("gradedBy", gradedBy);
        map.put("gradedAt", gradedAt);
        return map;
    }

    public static Submission fromMap(Map<String, Object> map) {
        Submission submission = new Submission();
        submission.setId((String) map.get("id"));
        submission.setAssignmentId((String) map.get("assignmentId"));
        submission.setStudentId((String) map.get("studentId"));
        submission.setStudentName((String) map.get("studentName"));
        submission.setFileUrl((String) map.get("fileUrl"));
        submission.setSubmittedAt((String) map.get("submittedAt"));
        submission.setGrade(map.get("grade") != null ? ((Number) map.get("grade")).doubleValue() : null);
        submission.setFeedback((String) map.get("feedback"));
        submission.setStatus((String) map.get("status"));
        submission.setGradedBy((String) map.get("gradedBy"));
        submission.setGradedAt((String) map.get("gradedAt"));
        return submission;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGradedBy() {
        return gradedBy;
    }

    public void setGradedBy(String gradedBy) {
        this.gradedBy = gradedBy;
    }

    public String getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(String gradedAt) {
        this.gradedAt = gradedAt;
    }
}
