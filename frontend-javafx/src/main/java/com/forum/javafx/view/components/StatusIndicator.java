package com.forum.javafx.view.components;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import com.forum.javafx.service.ForumApiService;

/**
 * Status indicator showing backend connection status
 */
public class StatusIndicator extends HBox {
    
    private Circle statusCircle;
    private Label statusLabel;
    private ForumApiService apiService;
    
    private static final Color CONNECTED_COLOR = Color.web("#51CF66");
    private static final Color DISCONNECTED_COLOR = Color.web("#FF6B6B");
    private static final Color CHECKING_COLOR = Color.web("#FFA94D");
    
    public StatusIndicator() {
        this.apiService = ForumApiService.getInstance();
        initialize();
        checkConnection();
    }
    
    private void initialize() {
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(8);
        setPadding(new Insets(5, 10, 5, 10));
        getStyleClass().add("status-indicator");
        
        // Status circle
        statusCircle = new Circle(5);
        statusCircle.setFill(CHECKING_COLOR);
        
        // Status label
        statusLabel = new Label("Checking connection...");
        statusLabel.getStyleClass().add("status-text");
        
        getChildren().addAll(statusCircle, statusLabel);
    }
    
    private void checkConnection() {
        new Thread(() -> {
            boolean connected = apiService.testConnection();
            Platform.runLater(() -> {
                updateStatus(connected);
            });
        }).start();
    }
    
    private void updateStatus(boolean connected) {
        Color newColor = connected ? CONNECTED_COLOR : DISCONNECTED_COLOR;
        String newText = connected ? "Connected to backend" : "Backend offline";
        
        // Animate color change
        FadeTransition fade = new FadeTransition(Duration.millis(300), statusCircle);
        fade.setFromValue(1.0);
        fade.setToValue(0.3);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
        fade.setOnFinished(e -> {
            statusCircle.setFill(newColor);
            statusLabel.setText(newText);
        });
        fade.play();
    }
    
    /**
     * Manually trigger a connection check
     */
    public void refresh() {
        statusCircle.setFill(CHECKING_COLOR);
        statusLabel.setText("Checking connection...");
        checkConnection();
    }
}
