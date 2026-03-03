package com.skillnest.user.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherProfile {
    private String userId;
    private String qualifications;
    private String bio;
    private double perClassRate;
    private List<String> specializations;
    private String experience;

    public TeacherProfile() {}

    public TeacherProfile(String userId, String qualifications, String bio, double perClassRate,
                          List<String> specializations, String experience) {
        this.userId = userId;
        this.qualifications = qualifications;
        this.bio = bio;
        this.perClassRate = perClassRate;
        this.specializations = specializations;
        this.experience = experience;
    }

    // Convert to Firestore Map
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId != null ? userId : "");
        map.put("qualifications", qualifications != null ? qualifications : "");
        map.put("bio", bio != null ? bio : "");
        map.put("perClassRate", perClassRate);
        map.put("specializations", specializations != null ? specializations : List.of());
        map.put("experience", experience != null ? experience : "");
        return map;
    }

    // Convert from Firestore Map
    @SuppressWarnings("unchecked")
    public static TeacherProfile fromMap(Map<String, Object> map) {
        TeacherProfile profile = new TeacherProfile();
        profile.setUserId((String) map.getOrDefault("userId", ""));
        profile.setQualifications((String) map.getOrDefault("qualifications", ""));
        profile.setBio((String) map.getOrDefault("bio", ""));

        Object rate = map.getOrDefault("perClassRate", 0.0);
        if (rate instanceof Number) {
            profile.setPerClassRate(((Number) rate).doubleValue());
        }

        Object specs = map.get("specializations");
        if (specs instanceof List) {
            profile.setSpecializations((List<String>) specs);
        } else {
            profile.setSpecializations(List.of());
        }

        profile.setExperience((String) map.getOrDefault("experience", ""));
        return profile;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public double getPerClassRate() { return perClassRate; }
    public void setPerClassRate(double perClassRate) { this.perClassRate = perClassRate; }
    public List<String> getSpecializations() { return specializations; }
    public void setSpecializations(List<String> specializations) { this.specializations = specializations; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
}
