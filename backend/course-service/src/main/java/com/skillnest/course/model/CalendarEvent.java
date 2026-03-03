package com.skillnest.course.model;

import java.util.HashMap;
import java.util.Map;

public class CalendarEvent {
    private String id;
    private String title;
    private String type; // class, assignment, event
    private String date;
    private String time;
    private String color;
    private String userId;
    private String courseId;

    public CalendarEvent() {}

    public CalendarEvent(String id, String title, String type, String date,
                         String time, String color, String userId, String courseId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.date = date;
        this.time = time;
        this.color = color;
        this.userId = userId;
        this.courseId = courseId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id != null ? id : "");
        map.put("title", title != null ? title : "");
        map.put("type", type != null ? type : "event");
        map.put("date", date != null ? date : "");
        map.put("time", time != null ? time : "");
        map.put("color", color != null ? color : "#4F46E5");
        map.put("userId", userId != null ? userId : "");
        map.put("courseId", courseId != null ? courseId : "");
        return map;
    }

    public static CalendarEvent fromMap(Map<String, Object> map) {
        CalendarEvent event = new CalendarEvent();
        event.setId((String) map.getOrDefault("id", ""));
        event.setTitle((String) map.getOrDefault("title", ""));
        event.setType((String) map.getOrDefault("type", "event"));
        event.setDate((String) map.getOrDefault("date", ""));
        event.setTime((String) map.getOrDefault("time", ""));
        event.setColor((String) map.getOrDefault("color", "#4F46E5"));
        event.setUserId((String) map.getOrDefault("userId", ""));
        event.setCourseId((String) map.getOrDefault("courseId", ""));
        return event;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
}
