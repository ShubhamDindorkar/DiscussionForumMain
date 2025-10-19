package com.forum.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

public class SignInController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;
    @FXML
    private Button googleButton;
    @FXML
    private Hyperlink signupLink;
    @FXML
    private Button emailOption;
    @FXML
    private Button phoneOption;
    @FXML
    private Button userpassOption;
    @FXML
    private TextField phoneField;

    @FXML
    private Label hintLabel;

    // Default credentials (change if you like)
    // Default user store (in-memory)
    private final java.util.Map<String, String> users = new java.util.HashMap<>();

    @FXML
    public void initialize() {
        // seed default admin
        users.put("admin", "admin");

        // option buttons
        emailOption.setOnAction(e -> showCredentialInput("email"));
        phoneOption.setOnAction(e -> showCredentialInput("phone"));
        userpassOption.setOnAction(e -> showCredentialInput("userpass"));
        signInButton.setOnAction(e -> {
            try {
                attemptSignIn();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        googleButton.setOnAction(e -> {
            // simulate OAuth flow success
            try {
                MainApp.showMainDashboard();
            } catch (IOException ex) {
                ex.printStackTrace();
                hintLabel.setText("Failed to open dashboard: " + ex.getMessage());
            }
        });

        signupLink.setOnAction(e -> {
            // simple sign-up: create user with provided usernameField and passwordField
            String u = usernameField.getText();
            String p = passwordField.getText();
            if (u == null || u.isBlank() || p == null || p.isBlank()) {
                hintLabel.setText("Provide username and password to sign up.");
                return;
            }
            users.put(u, p);
            hintLabel.setText("Account created. You can sign in with username: '" + u + "'.");
        });
    }

    private void attemptSignIn() throws IOException {
        String u = usernameField.getText();
        String p = passwordField.getText();
        if (u == null) u = "";
        if (p == null) p = "";
        String stored = users.get(u);
        if (stored != null && stored.equals(p)) {
            // proceed to main dashboard
            try {
                MainApp.showMainDashboard();
            } catch (IOException ex) {
                ex.printStackTrace();
                hintLabel.setText("Failed to open dashboard: " + ex.getMessage());
            }
        } else {
            hintLabel.setText("Invalid credentials. Try username: 'admin' and password: 'admin'");
        }
    }

    private void showCredentialInput(String mode) {
        if ("phone".equals(mode)) {
            phoneField.setVisible(true); phoneField.setManaged(true);
            usernameField.setVisible(false); usernameField.setManaged(false);
        } else {
            phoneField.setVisible(false); phoneField.setManaged(false);
            usernameField.setVisible(true); usernameField.setManaged(true);
        }
        // for this demo, email and userpass share same inputs
    }
}
