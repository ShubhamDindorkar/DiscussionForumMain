package com.forum.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.util.HashMap;
import java.util.Map;

public class StudentSignInController {
    @FXML private Button googleButton;
    @FXML private Button emailOption;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Hyperlink signupLink;
    @FXML private Hyperlink adminLoginLink;
    @FXML private Text errorText;
    
    // Mock student database
    private static final Map<String, String> studentCredentials = new HashMap<>();
    
    static {
        studentCredentials.put("student", "student123");
        studentCredentials.put("john.doe", "password");
        studentCredentials.put("S001", "student");
    }
    
    @FXML
    public void initialize() {
        // Google sign-in handler
        googleButton.setOnAction(e -> handleGoogleSignIn());
        
        // Email sign-in handler
        emailOption.setOnAction(e -> handleEmailSignIn());
    }
    
    @FXML
    private void handleStudentSignIn() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        // Validate credentials
        if (studentCredentials.containsKey(username) && 
            studentCredentials.get(username).equals(password)) {
            // Success - navigate to student dashboard
            MainApp.showStudentDashboard(username);
        } else {
            showError("Invalid credentials. Please try again.");
        }
    }
    
    private void handleGoogleSignIn() {
        // Mock Google OAuth - in real app, this would trigger OAuth flow
        showInfo("Google Sign-In functionality would be implemented here");
        // For demo, auto-login as student
        MainApp.showStudentDashboard("google.user");
    }
    
    private void handleEmailSignIn() {
        // Mock email sign-in
        showInfo("Email Sign-In functionality would be implemented here");
    }
    
    @FXML
    private void switchToAdminLogin() {
        MainApp.showAdminSignIn();
    }
    
    private void showError(String message) {
        errorText.setText(message);
        errorText.setVisible(true);
        errorText.setStyle("-fx-fill: #e53e3e;");
    }
    
    private void showInfo(String message) {
        errorText.setText(message);
        errorText.setVisible(true);
        errorText.setStyle("-fx-fill: #3182ce;");
    }
}
