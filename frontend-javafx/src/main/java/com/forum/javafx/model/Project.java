package com.forum.javafx.model;

public class Project {
    private String id;
    private String name;
    private String description;
    private String status; // ONGOING, COMPLETED, PAUSED
    private int progress; // 0-100
    private String language;
    private String startDate;
    private String dueDate;
    
    public Project() {
    }
    
    public Project(String id, String name, String description, String status, int progress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.progress = progress;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
}
