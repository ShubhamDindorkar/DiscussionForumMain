package com.forum.javafx.model;

import java.util.Date;

public class Workspace {
    private String id;
    private String name;
    private String description;
    private String userId;
    private Date createdDate;
    private String repositoryUrl;
    private boolean isPrivate;
    private String language;
    
    public Workspace() {
        this.createdDate = new Date();
        this.isPrivate = true;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    
    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }
    
    public boolean isPrivate() { return isPrivate; }
    public void setPrivate(boolean aPrivate) { isPrivate = aPrivate; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
