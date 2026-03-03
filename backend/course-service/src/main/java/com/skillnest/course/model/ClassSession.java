package com.skillnest.course.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassSession {
    private String id;
    private String courseId;
    private String title;
    private String date;
    private String time;
    private int duration; // in minutes
    private String meetLink;
    private String status; // scheduled, live, completed
    private List<String> students;
    private long createdAt;

    public ClassSession() {
        this.students = new ArrayList<>();
    }

    public ClassSession(String id, String courseId, String title, String date,
                        String time, int duration, String meetLink) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.meetLink = meetLink;
        this.status = "scheduled";
        this.students = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id != null ? id : "");
        map.put("courseId", courseId != null ? courseId : "");
        map.put("title", title != null ? title : "");
        map.put("date", date != null ? date : "");
        map.put("time", time != null ? time : "");
        map.put("duration", duration);
        map.put("meetLink", meetLink != null ? meetLink : "");
        map.put("status", status != null ? status : "scheduled");
        map.put("students", students != null ? students : new ArrayList<>());
        map.put("createdAt", createdAt);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static ClassSession fromMap(Map<String, Object> map) {
        ClassSession session = new ClassSession();
        session.setId((String) map.getOrDefault("id", ""));
        session.setCourseId((String) map.getOrDefault("courseId", ""));
        session.setTitle((String) map.getOrDefault("title", ""));
        session.setDate((String) map.getOrDefault("date", ""));
        session.setTime((String) map.getOrDefault("time", ""));
        session.setDuration(((Number) map.getOrDefault("duration", 0)).intValue());
        session.setMeetLink((String) map.getOrDefault("meetLink", ""));
        session.setStatus((String) map.getOrDefault("status", "scheduled"));
        Object studentsObj = map.get("students");
        if (studentsObj instanceof List) {
            session.setStudents((List<String>) studentsObj);
        } else {
            session.setStudents(new ArrayList<>());
        }
        session.setCreatedAt(((Number) map.getOrDefault("createdAt", 0L)).longValue());
        return session;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getMeetLink() { return meetLink; }
    public void setMeetLink(String meetLink) { this.meetLink = meetLink; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getStudents() { return students; }
    public void setStudents(List<String> students) { this.students = students; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
