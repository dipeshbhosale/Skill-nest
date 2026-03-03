package com.skillnest.user.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentProfile {
    private String userId;
    private List<String> enrolledCourses;
    private String gradeLevel;
    private List<String> interests;

    public StudentProfile() {}

    public StudentProfile(String userId, List<String> enrolledCourses, String gradeLevel, List<String> interests) {
        this.userId = userId;
        this.enrolledCourses = enrolledCourses;
        this.gradeLevel = gradeLevel;
        this.interests = interests;
    }

    // Convert to Firestore Map
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId != null ? userId : "");
        map.put("enrolledCourses", enrolledCourses != null ? enrolledCourses : List.of());
        map.put("gradeLevel", gradeLevel != null ? gradeLevel : "");
        map.put("interests", interests != null ? interests : List.of());
        return map;
    }

    // Convert from Firestore Map
    @SuppressWarnings("unchecked")
    public static StudentProfile fromMap(Map<String, Object> map) {
        StudentProfile profile = new StudentProfile();
        profile.setUserId((String) map.getOrDefault("userId", ""));

        Object courses = map.get("enrolledCourses");
        if (courses instanceof List) {
            profile.setEnrolledCourses((List<String>) courses);
        } else {
            profile.setEnrolledCourses(List.of());
        }

        profile.setGradeLevel((String) map.getOrDefault("gradeLevel", ""));

        Object interests = map.get("interests");
        if (interests instanceof List) {
            profile.setInterests((List<String>) interests);
        } else {
            profile.setInterests(List.of());
        }

        return profile;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<String> getEnrolledCourses() { return enrolledCourses; }
    public void setEnrolledCourses(List<String> enrolledCourses) { this.enrolledCourses = enrolledCourses; }
    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }
    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }
}
