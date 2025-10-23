package com.forum.javafx.model;

public class User {
    private String id;
    private String name;
    private String email;
    private String profilePicture;
    private UserRole role;
    private String authProvider; // GOOGLE, LOCAL
    
    public enum UserRole {
        ADMIN, STUDENT
    }
    
    public User() {
    }
    
    public User(String id, String name, String email, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
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
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public String getAuthProvider() { return authProvider; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }
}
