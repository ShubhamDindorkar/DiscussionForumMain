package com.forum.javafx.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import com.forum.javafx.view.components.HeaderView;
import com.forum.javafx.view.components.TopicListView;
import com.forum.javafx.view.components.MessagesView;
import com.forum.javafx.model.Topic;

public class MainView extends BorderPane {
    
    private HeaderView headerView;
    private TopicListView topicListView;
    private MessagesView messagesView;
    private StackPane contentArea;
    
    public MainView() {
        initialize();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initialize() {
        getStyleClass().add("main-view");
        
        // Initialize components
        headerView = new HeaderView();
        topicListView = new TopicListView();
        messagesView = new MessagesView();
        
        contentArea = new StackPane();
        contentArea.getStyleClass().add("content-area");
    }
    
    private void setupLayout() {
        // Set header at top
        setTop(headerView);
        
        // Create split pane for topics and messages
        SplitPane splitPane = new SplitPane();
        splitPane.getStyleClass().add("main-split-pane");
        
        // Left side: Topics list
        VBox topicsContainer = new VBox();
        topicsContainer.getStyleClass().add("topics-container");
        topicsContainer.getChildren().add(topicListView);
        VBox.setVgrow(topicListView, Priority.ALWAYS);
        
        // Right side: Content area
        contentArea.getChildren().add(createWelcomeView());
        
        splitPane.getItems().addAll(topicsContainer, contentArea);
        splitPane.setDividerPositions(0.35);
        
        setCenter(splitPane);
    }
    
    private void setupEventHandlers() {
        // Handle topic selection
        topicListView.setOnTopicSelected(topic -> {
            showMessages(topic);
        });
        
        // Handle new topic creation
        headerView.setOnNewTopicAction(() -> {
            topicListView.showCreateTopicDialog();
        });
        
        // Handle refresh action
        headerView.setOnRefreshAction(() -> {
            topicListView.refreshTopics();
        });
        
        // Handle topic created
        topicListView.setOnTopicCreated(topic -> {
            // Automatically select the newly created topic
            topicListView.selectTopic(topic);
        });
        
        // Handle back to topics from messages view
        messagesView.setOnBackToTopics(() -> {
            showWelcomeView();
        });
    }
    
    private VBox createWelcomeView() {
        VBox welcome = new VBox(30);
        welcome.setAlignment(Pos.CENTER);
        welcome.getStyleClass().add("welcome-view");
        
        Label titleLabel = new Label("Welcome to Discussion Forum");
        titleLabel.getStyleClass().add("welcome-title");
        
        Label subtitleLabel = new Label("Select a topic from the left to view messages");
        subtitleLabel.getStyleClass().add("welcome-subtitle");
        
        Label instructionLabel = new Label("or create a new topic to start a discussion");
        instructionLabel.getStyleClass().add("welcome-instruction");
        
        // Icon placeholder
        Label iconLabel = new Label("💬");
        iconLabel.setStyle("-fx-font-size: 72px;");
        
        welcome.getChildren().addAll(iconLabel, titleLabel, subtitleLabel, instructionLabel);
        
        return welcome;
    }
    
    private void showMessages(Topic topic) {
        messagesView.loadMessages(topic);
        
        // Animate transition
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), contentArea);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(messagesView);
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contentArea);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
    
    private void showWelcomeView() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), contentArea);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(createWelcomeView());
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contentArea);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
    
    public void playEntranceAnimation() {
        setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), this);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}
