package com.skillnest.course.model;

import java.util.HashMap;
import java.util.Map;

public class Material {
    private String id;
    private String courseId;
    private String title;
    private String type; // PDF, DOCX, PPTX
    private String fileUrl;
    private long size;
    private String uploadedBy;
    private long uploadedAt;
    private int downloads;

    public Material() {}

    public Material(String id, String courseId, String title, String type,
                    String fileUrl, long size, String uploadedBy) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.type = type;
        this.fileUrl = fileUrl;
        this.size = size;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = System.currentTimeMillis();
        this.downloads = 0;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id != null ? id : "");
        map.put("courseId", courseId != null ? courseId : "");
        map.put("title", title != null ? title : "");
        map.put("type", type != null ? type : "");
        map.put("fileUrl", fileUrl != null ? fileUrl : "");
        map.put("size", size);
        map.put("uploadedBy", uploadedBy != null ? uploadedBy : "");
        map.put("uploadedAt", uploadedAt);
        map.put("downloads", downloads);
        return map;
    }

    public static Material fromMap(Map<String, Object> map) {
        Material material = new Material();
        material.setId((String) map.getOrDefault("id", ""));
        material.setCourseId((String) map.getOrDefault("courseId", ""));
        material.setTitle((String) map.getOrDefault("title", ""));
        material.setType((String) map.getOrDefault("type", ""));
        material.setFileUrl((String) map.getOrDefault("fileUrl", ""));
        material.setSize(((Number) map.getOrDefault("size", 0L)).longValue());
        material.setUploadedBy((String) map.getOrDefault("uploadedBy", ""));
        material.setUploadedAt(((Number) map.getOrDefault("uploadedAt", 0L)).longValue());
        material.setDownloads(((Number) map.getOrDefault("downloads", 0)).intValue());
        return material;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
    public long getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(long uploadedAt) { this.uploadedAt = uploadedAt; }
    public int getDownloads() { return downloads; }
    public void setDownloads(int downloads) { this.downloads = downloads; }
}
