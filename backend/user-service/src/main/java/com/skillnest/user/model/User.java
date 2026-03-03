package com.skillnest.user.model;

import java.util.Map;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role; // admin, teacher, student
    private String phone;
    private String status; // active, inactive
    private long createdAt;
    private long updatedAt;

    public User() {}

    public User(String id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = "active";
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Convert to Firestore Map
    public Map<String, Object> toMap() {
        return Map.of(
            "id", id != null ? id : "",
            "name", name != null ? name : "",
            "email", email != null ? email : "",
            "password", password != null ? password : "",
            "role", role != null ? role : "student",
            "phone", phone != null ? phone : "",
            "status", status != null ? status : "active",
            "createdAt", createdAt,
            "updatedAt", updatedAt
        );
    }

    // Convert from Firestore Map
    public static User fromMap(Map<String, Object> map) {
        User user = new User();
        user.setId((String) map.getOrDefault("id", ""));
        user.setName((String) map.getOrDefault("name", ""));
        user.setEmail((String) map.getOrDefault("email", ""));
        user.setPassword((String) map.getOrDefault("password", ""));
        user.setRole((String) map.getOrDefault("role", "student"));
        user.setPhone((String) map.getOrDefault("phone", ""));
        user.setStatus((String) map.getOrDefault("status", "active"));
        user.setCreatedAt((Long) map.getOrDefault("createdAt", 0L));
        user.setUpdatedAt((Long) map.getOrDefault("updatedAt", 0L));
        return user;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
