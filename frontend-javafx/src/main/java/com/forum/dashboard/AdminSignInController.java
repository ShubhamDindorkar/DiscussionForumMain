package com.forum.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class AdminSignInController {
    @FXML private TextField adminUsernameField;
    @FXML private PasswordField adminPasswordField;
    @FXML private Hyperlink studentLoginLink;
    @FXML private Text errorText;
    
    // Secure admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    
    @FXML
    private void handleAdminSignIn() {
        String username = adminUsernameField.getText().trim();
        String password = adminPasswordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        // Validate admin credentials
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            // Success - navigate to admin dashboard
            MainApp.showAdminDashboard();
        } else {
            showError("Invalid admin credentials. Access denied.");
        }
    }
    
    @FXML
    private void switchToStudentLogin() {
        MainApp.showStudentSignIn();
    }
    
    private void showError(String message) {
        errorText.setText(message);
        errorText.setVisible(true);
    }
}
