package com.forum.javafx.model;

import java.util.Date;

public class CalendarEvent {
    private String id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private EventType type;
    private String userId;
    private boolean isCompleted;
    private Priority priority;
    
    public enum EventType {
        TASK, EXAM, ASSIGNMENT, MEETING, OTHER
    }
    
    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }
    
    public CalendarEvent() {
        this.isCompleted = false;
        this.priority = Priority.MEDIUM;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    
    public EventType getType() { return type; }
    public void setType(EventType type) { this.type = type; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
}
