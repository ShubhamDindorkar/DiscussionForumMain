package com.forum.javafx.model;

public class Club {
    private String id;
    private String name;
    private String description;
    private String color;
    private int memberCount;
    private int topicCount;
    private String category;
    
    public Club() {
    }
    
    public Club(String id, String name, String description, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    
    public int getTopicCount() { return topicCount; }
    public void setTopicCount(int topicCount) { this.topicCount = topicCount; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
