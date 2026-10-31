package com.hospital.management.model;

import java.time.LocalDateTime;

public class Notification {
    private String id;
    private String title;
    private String message;
    private String type; // INFO, WARNING, SUCCESS, ERROR
    private String targetRole; // ALL, ADMIN, DOCTOR, RECEPTIONIST, PATIENT
    private boolean read;
    private LocalDateTime createdAt;
    private String icon;
    private String link;

    public Notification() {}

    public Notification(String id, String title, String message, String type, String targetRole) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.targetRole = targetRole;
        this.read = false;
        this.createdAt = LocalDateTime.now();
        assignIcon();
    }

    private void assignIcon() {
        this.icon = switch (type) {
            case "WARNING" -> "exclamation-triangle";
            case "SUCCESS" -> "check-circle";
            case "ERROR" -> "times-circle";
            default -> "info-circle";
        };
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
}
