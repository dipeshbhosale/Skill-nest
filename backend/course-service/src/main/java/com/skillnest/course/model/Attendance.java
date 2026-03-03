package com.skillnest.course.model;

import java.util.HashMap;
import java.util.Map;

public class Attendance {
    private String id;
    private String classId;
    private String studentId;
    private String courseId;
    private String date;
    private String status; // present, absent
    private String markedBy;

    public Attendance() {}

    public Attendance(String id, String classId, String studentId, String courseId,
                      String date, String status, String markedBy) {
        this.id = id;
        this.classId = classId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.date = date;
        this.status = status;
        this.markedBy = markedBy;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id != null ? id : "");
        map.put("classId", classId != null ? classId : "");
        map.put("studentId", studentId != null ? studentId : "");
        map.put("courseId", courseId != null ? courseId : "");
        map.put("date", date != null ? date : "");
        map.put("status", status != null ? status : "absent");
        map.put("markedBy", markedBy != null ? markedBy : "");
        return map;
    }

    public static Attendance fromMap(Map<String, Object> map) {
        Attendance attendance = new Attendance();
        attendance.setId((String) map.getOrDefault("id", ""));
        attendance.setClassId((String) map.getOrDefault("classId", ""));
        attendance.setStudentId((String) map.getOrDefault("studentId", ""));
        attendance.setCourseId((String) map.getOrDefault("courseId", ""));
        attendance.setDate((String) map.getOrDefault("date", ""));
        attendance.setStatus((String) map.getOrDefault("status", "absent"));
        attendance.setMarkedBy((String) map.getOrDefault("markedBy", ""));
        return attendance;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMarkedBy() { return markedBy; }
    public void setMarkedBy(String markedBy) { this.markedBy = markedBy; }
}
