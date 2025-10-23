package com.forum.javafx.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import com.forum.javafx.service.AuthService;

public class LoginView extends StackPane {
    
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField googleEmailField;
    private Button loginButton;
    private Button googleLoginButton;
    private AuthService authService;
    private Runnable onLoginSuccess;
    
    public LoginView() {
        this.authService = AuthService.getInstance();
        initialize();
        setupLayout();
        playEntranceAnimation();
    }
    
    private void initialize() {
        getStyleClass().add("login-view");
    }
    
    private void setupLayout() {
        // Main container
        VBox mainContainer = new VBox(40);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setMaxWidth(500);
        mainContainer.getStyleClass().add("login-container");
        
        // College Logo and Title (Above the card)
        VBox headerBox = new VBox(15);
        headerBox.setAlignment(Pos.CENTER);
        
        // College logo - use actual image (BIGGER)
        ImageView logoView = new ImageView();
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/college-logo.png"));
            logoView.setImage(logoImage);
            logoView.setFitWidth(150);
            logoView.setFitHeight(150);
            logoView.setPreserveRatio(true);
            logoView.getStyleClass().add("login-logo");
        } catch (Exception e) {
            // Fallback to emoji if image not found
            Label logoLabel = new Label("🎓");
            logoLabel.getStyleClass().add("login-logo");
            logoLabel.setStyle("-fx-font-size: 120px;");
            headerBox.getChildren().add(logoLabel);
        }
        
        if (logoView.getImage() != null) {
            headerBox.getChildren().add(logoView);
        }
        
        Label forumLabel = new Label("Discussion Forum");
        forumLabel.getStyleClass().add("login-title");
        forumLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Label collegeNameLabel = new Label("College Community Platform");
        collegeNameLabel.getStyleClass().add("login-subtitle");
        collegeNameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: 500;");
        
        headerBox.getChildren().addAll(forumLabel, collegeNameLabel);
        
        // Login form card
        VBox loginCard = new VBox(25);
        loginCard.getStyleClass().add("login-card");
        loginCard.setPadding(new Insets(45, 45, 45, 45));
        
        // Welcome message inside card
        Label welcomeLabel = new Label("Welcome to Discussion Forum");
        welcomeLabel.getStyleClass().add("card-welcome-text");
        
        Label instructionLabel = new Label("Please login to continue");
        instructionLabel.getStyleClass().add("card-instruction-text");
        
        VBox welcomeBox = new VBox(8);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.getChildren().addAll(welcomeLabel, instructionLabel);
        
        // Standard Login Section
        Label loginLabel = new Label("Login with Credentials");
        loginLabel.getStyleClass().add("section-label");
        
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("login-field");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("login-field");
        
        Label hintLabel = new Label("Use: admin / admin");
        hintLabel.getStyleClass().add("hint-label");
        
        loginButton = new Button("Login");
        loginButton.getStyleClass().addAll("login-button", "primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        
        VBox loginSection = new VBox(12);
        loginSection.getChildren().addAll(loginLabel, usernameField, passwordField, hintLabel, loginButton);
        
        // Separator
        HBox separatorBox = new HBox(10);
        separatorBox.setAlignment(Pos.CENTER);
        Separator sep1 = new Separator();
        HBox.setHgrow(sep1, Priority.ALWAYS);
        Label orLabel = new Label("OR");
        orLabel.getStyleClass().add("or-label");
        Separator sep2 = new Separator();
        HBox.setHgrow(sep2, Priority.ALWAYS);
        separatorBox.getChildren().addAll(sep1, orLabel, sep2);
        
        // Google Login Section
        Label googleLabel = new Label("Student Login (Google)");
        googleLabel.getStyleClass().add("section-label");
        
        googleEmailField = new TextField();
        googleEmailField.setPromptText("College Email (e.g., student@college.edu)");
        googleEmailField.getStyleClass().add("login-field");
        
        googleLoginButton = new Button("🔐 Sign in with Google");
        googleLoginButton.getStyleClass().addAll("login-button", "google-button");
        googleLoginButton.setMaxWidth(Double.MAX_VALUE);
        
        VBox googleSection = new VBox(12);
        googleSection.getChildren().addAll(googleLabel, googleEmailField, googleLoginButton);
        
        loginCard.getChildren().addAll(welcomeBox, loginSection, separatorBox, googleSection);
        
        mainContainer.getChildren().addAll(headerBox, loginCard);
        
        getChildren().add(mainContainer);
        
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        loginButton.setOnAction(e -> handleAdminLogin());
        googleLoginButton.setOnAction(e -> handleGoogleLogin());
        
        // Enter key support
        usernameField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> handleAdminLogin());
        googleEmailField.setOnAction(e -> handleGoogleLogin());
    }
    
    private void handleAdminLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password");
            return;
        }
        
        if (authService.login(username, password)) {
            if (onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        } else {
            showError("Invalid credentials. Try admin/admin");
            passwordField.clear();
        }
    }
    
    private void handleGoogleLogin() {
        String email = googleEmailField.getText().trim();
        
        if (email.isEmpty()) {
            showError("Please enter your college email");
            return;
        }
        
        if (!email.contains("@")) {
            showError("Please enter a valid email address");
            return;
        }
        
        if (authService.loginWithGoogle(email)) {
            if (onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        } else {
            showError("Login failed. Please try again");
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void playEntranceAnimation() {
        setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(800), this);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }
    
    public void setOnLoginSuccess(Runnable handler) {
        this.onLoginSuccess = handler;
    }
}
