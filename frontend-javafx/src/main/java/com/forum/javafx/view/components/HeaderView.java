package com.forum.javafx.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HeaderView extends VBox {
    
    private Button newTopicButton;
    private Button refreshButton;
    
    private Runnable onNewTopicAction;
    private Runnable onRefreshAction;
    
    public HeaderView() {
        initialize();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initialize() {
        getStyleClass().add("header-view");
        setPadding(new Insets(20, 25, 15, 25));
    }
    
    private void setupLayout() {
        // Title and buttons row
        HBox headerRow = new HBox(15);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        
        // App title
        Label titleLabel = new Label("Discussion Forum");
        titleLabel.getStyleClass().add("app-title");
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Refresh button
        refreshButton = new Button("⟳ Refresh");
        refreshButton.getStyleClass().addAll("header-button", "refresh-button");
        
        // New topic button
        newTopicButton = new Button("+ New Topic");
        newTopicButton.getStyleClass().addAll("header-button", "primary-button");
        
        headerRow.getChildren().addAll(titleLabel, spacer, refreshButton, newTopicButton);
        
        // Separator
        Separator separator = new Separator();
        separator.getStyleClass().add("header-separator");
        
        getChildren().addAll(headerRow, separator);
    }
    
    private void setupEventHandlers() {
        newTopicButton.setOnAction(e -> {
            if (onNewTopicAction != null) {
                onNewTopicAction.run();
            }
        });
        
        refreshButton.setOnAction(e -> {
            if (onRefreshAction != null) {
                onRefreshAction.run();
            }
        });
    }
    
    public void setOnNewTopicAction(Runnable action) {
        this.onNewTopicAction = action;
    }
    
    public void setOnRefreshAction(Runnable action) {
        this.onRefreshAction = action;
    }
}
