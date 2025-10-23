package com.forum.javafx.view.components;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import com.forum.javafx.model.Message;
import com.forum.javafx.model.Topic;
import com.forum.javafx.service.ForumApiService;

import java.text.SimpleDateFormat;
import java.util.List;

public class MessagesView extends BorderPane {
    
    private Topic currentTopic;
    private VBox messagesContainer;
    private ScrollPane scrollPane;
    private TextArea messageInput;
    private Button sendButton;
    private Button backButton;
    private Label topicTitleLabel;
    private ProgressIndicator loadingIndicator;
    private ForumApiService apiService;
    
    private Runnable onBackToTopics;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
    
    public MessagesView() {
        this.apiService = ForumApiService.getInstance();
        initialize();
        setupLayout();
    }
    
    private void initialize() {
        getStyleClass().add("messages-view");
    }
    
    private void setupLayout() {
        // Top bar with back button and topic title
        HBox topBar = new HBox(15);
        topBar.getStyleClass().add("messages-top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15, 20, 15, 20));
        
        backButton = new Button("← Back");
        backButton.getStyleClass().addAll("back-button", "header-button");
        backButton.setOnAction(e -> {
            if (onBackToTopics != null) {
                onBackToTopics.run();
            }
        });
        
        VBox titleContainer = new VBox(3);
        Label label = new Label("Topic");
        label.getStyleClass().add("topic-label");
        
        topicTitleLabel = new Label();
        topicTitleLabel.getStyleClass().add("topic-title-label");
        
        titleContainer.getChildren().addAll(label, topicTitleLabel);
        
        topBar.getChildren().addAll(backButton, titleContainer);
        setTop(topBar);
        
        // Messages area
        messagesContainer = new VBox(12);
        messagesContainer.getStyleClass().add("messages-container");
        messagesContainer.setPadding(new Insets(20));
        
        scrollPane = new ScrollPane(messagesContainer);
        scrollPane.getStyleClass().add("messages-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        // Loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(50, 50);
        
        StackPane centerPane = new StackPane();
        centerPane.getChildren().addAll(scrollPane, loadingIndicator);
        loadingIndicator.setVisible(false);
        
        setCenter(centerPane);
        
        // Input area at bottom
        VBox inputArea = new VBox(10);
        inputArea.getStyleClass().add("message-input-area");
        inputArea.setPadding(new Insets(15, 20, 15, 20));
        
        Label inputLabel = new Label("Write your message:");
        inputLabel.getStyleClass().add("input-label");
        
        messageInput = new TextArea();
        messageInput.getStyleClass().add("message-input");
        messageInput.setPromptText("Type your message here...");
        messageInput.setPrefRowCount(3);
        messageInput.setWrapText(true);
        
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        
        sendButton = new Button("Send Message");
        sendButton.getStyleClass().addAll("send-button", "primary-button");
        sendButton.setDisable(true);
        sendButton.setOnAction(e -> sendMessage());
        
        buttonBar.getChildren().add(sendButton);
        
        inputArea.getChildren().addAll(inputLabel, messageInput, buttonBar);
        setBottom(inputArea);
        
        // Enable send button only when there's text
        messageInput.textProperty().addListener((obs, oldVal, newVal) -> {
            sendButton.setDisable(newVal.trim().isEmpty());
        });
        
        // Send on Ctrl+Enter
        messageInput.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode().toString().equals("ENTER")) {
                if (!sendButton.isDisabled()) {
                    sendMessage();
                }
            }
        });
    }
    
    public void loadMessages(Topic topic) {
        this.currentTopic = topic;
        topicTitleLabel.setText(topic.getTitle());
        messagesContainer.getChildren().clear();
        messageInput.clear();
        
        showLoading(true);
        
        new Thread(() -> {
            try {
                List<Message> messages = apiService.getMessagesForTopic(topic.getId());
                Platform.runLater(() -> {
                    displayMessages(messages);
                    showLoading(false);
                    scrollToBottom();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Failed to load messages: " + e.getMessage());
                    showLoading(false);
                });
            }
        }).start();
    }
    
    private void displayMessages(List<Message> messages) {
        messagesContainer.getChildren().clear();
        
        if (messages.isEmpty()) {
            Label emptyLabel = new Label("No messages yet.\nBe the first to post!");
            emptyLabel.getStyleClass().add("empty-messages-label");
            emptyLabel.setAlignment(Pos.CENTER);
            messagesContainer.getChildren().add(emptyLabel);
            VBox.setVgrow(emptyLabel, Priority.ALWAYS);
        } else {
            for (Message message : messages) {
                MessageCard card = new MessageCard(message);
                messagesContainer.getChildren().add(card);
                
                // Animate entrance
                card.setOpacity(0);
                FadeTransition fade = new FadeTransition(Duration.millis(300), card);
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.play();
            }
        }
    }
    
    private void sendMessage() {
        String content = messageInput.getText().trim();
        if (content.isEmpty() || currentTopic == null) {
            return;
        }
        
        sendButton.setDisable(true);
        messageInput.setDisable(true);
        
        new Thread(() -> {
            try {
                Message newMessage = apiService.addMessage(currentTopic.getId(), content);
                Platform.runLater(() -> {
                    messageInput.clear();
                    messageInput.setDisable(false);
                    loadMessages(currentTopic); // Reload to show new message
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Failed to send message: " + e.getMessage());
                    messageInput.setDisable(false);
                    sendButton.setDisable(false);
                });
            }
        }).start();
    }
    
    private void scrollToBottom() {
        Platform.runLater(() -> {
            scrollPane.setVvalue(1.0);
        });
    }
    
    private void showLoading(boolean show) {
        loadingIndicator.setVisible(show);
        scrollPane.setVisible(!show);
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void setOnBackToTopics(Runnable handler) {
        this.onBackToTopics = handler;
    }
    
    // Inner class for Message Card
    private static class MessageCard extends VBox {
        
        public MessageCard(Message message) {
            setupCard(message);
        }
        
        private void setupCard(Message message) {
            getStyleClass().add("message-card");
            setPadding(new Insets(15, 18, 15, 18));
            setSpacing(8);
            
            // Message content
            Label contentLabel = new Label(message.getContent());
            contentLabel.getStyleClass().add("message-content");
            contentLabel.setWrapText(true);
            contentLabel.setMaxWidth(Double.MAX_VALUE);
            
            // Timestamp
            String timestamp = message.getCreateTime() != null ? 
                             dateFormat.format(message.getCreateTime()) : "Unknown time";
            Label timeLabel = new Label(timestamp);
            timeLabel.getStyleClass().add("message-timestamp");
            
            // Separator line
            Separator separator = new Separator();
            separator.getStyleClass().add("message-separator");
            
            getChildren().addAll(contentLabel, separator, timeLabel);
        }
    }
}
