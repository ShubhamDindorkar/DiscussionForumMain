package com.forum.javafx.service;

import com.forum.javafx.model.User;

public class AuthService {
    private static AuthService instance;
    private User currentUser;
    
    private AuthService() {}
    
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }
    
    public boolean login(String username, String password) {
        // Simple authentication - admin/admin
        if ("admin".equals(username) && "admin".equals(password)) {
            currentUser = new User("1", "Administrator", "admin@forum.com", User.UserRole.ADMIN);
            currentUser.setAuthProvider("LOCAL");
            return true;
        }
        return false;
    }
    
    public boolean loginWithGoogle(String email) {
        // Simulate Google OAuth - accept any email ending with college domain
        if (email != null && email.contains("@")) {
            currentUser = new User("google_" + email, email.split("@")[0], email, User.UserRole.STUDENT);
            currentUser.setAuthProvider("GOOGLE");
            return true;
        }
        return false;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
