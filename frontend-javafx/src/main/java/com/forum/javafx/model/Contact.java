package com.forum.javafx.model;

public class Contact {
    private String id;
    private String name;
    private String email;
    private String profilePicture;
    private String status; // online, offline, away
    private String lastMessage;
    private String lastMessageTime;
    private int unreadCount;
    
    public Contact() {
    }
    
    public Contact(String id, String name, String email, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.unreadCount = 0;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    
    public String getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(String lastMessageTime) { this.lastMessageTime = lastMessageTime; }
    
    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
}
