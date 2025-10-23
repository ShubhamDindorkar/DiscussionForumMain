package com.forum.javafx.view.components;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import com.forum.javafx.model.Topic;
import com.forum.javafx.service.ForumApiService;

import java.util.List;
import java.util.function.Consumer;

public class TopicListView extends VBox {
    
    private VBox topicsContainer;
    private ProgressIndicator loadingIndicator;
    private Label statusLabel;
    private ForumApiService apiService;
    
    private Consumer<Topic> onTopicSelected;
    private Consumer<Topic> onTopicCreated;
    private Topic selectedTopic;
    
    public TopicListView() {
        this.apiService = ForumApiService.getInstance();
        initialize();
        setupLayout();
        loadTopics();
    }
    
    private void initialize() {
        getStyleClass().add("topic-list-view");
        setPadding(new Insets(15));
        setSpacing(10);
    }
    
    private void setupLayout() {
        // Header
        Label headerLabel = new Label("Topics");
        headerLabel.getStyleClass().add("section-title");
        
        // Status label
        statusLabel = new Label();
        statusLabel.getStyleClass().add("status-label");
        statusLabel.setVisible(false);
        
        // Loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.getStyleClass().add("loading-indicator");
        loadingIndicator.setMaxSize(40, 40);
        loadingIndicator.setVisible(false);
        
        StackPane loadingPane = new StackPane(loadingIndicator);
        loadingPane.setAlignment(Pos.CENTER);
        loadingPane.setPadding(new Insets(20));
        
        // Topics container with scroll
        topicsContainer = new VBox(8);
        topicsContainer.getStyleClass().add("topics-list");
        
        ScrollPane scrollPane = new ScrollPane(topicsContainer);
        scrollPane.getStyleClass().add("topics-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        getChildren().addAll(headerLabel, statusLabel, loadingPane, scrollPane);
    }
    
    public void loadTopics() {
        showLoading(true);
        statusLabel.setVisible(false);
        
        new Thread(() -> {
            try {
                List<Topic> topics = apiService.getAllTopics();
                Platform.runLater(() -> {
                    displayTopics(topics);
                    showLoading(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Failed to load topics. Make sure backend is running.");
                    showLoading(false);
                });
            }
        }).start();
    }
    
    private void displayTopics(List<Topic> topics) {
        topicsContainer.getChildren().clear();
        
        if (topics.isEmpty()) {
            Label emptyLabel = new Label("No topics yet.\nCreate your first topic!");
            emptyLabel.getStyleClass().add("empty-label");
            emptyLabel.setAlignment(Pos.CENTER);
            emptyLabel.setMaxWidth(Double.MAX_VALUE);
            topicsContainer.getChildren().add(emptyLabel);
        } else {
            for (Topic topic : topics) {
                TopicCard card = new TopicCard(topic);
                card.setOnMouseClicked(e -> selectTopicCard(card, topic));
                topicsContainer.getChildren().add(card);
                
                // Animate card entrance
                card.setOpacity(0);
                card.setScaleX(0.9);
                card.setScaleY(0.9);
                
                javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(Duration.millis(300), card);
                fade.setFromValue(0);
                fade.setToValue(1);
                
                ScaleTransition scale = new ScaleTransition(Duration.millis(300), card);
                scale.setFromX(0.9);
                scale.setFromY(0.9);
                scale.setToX(1.0);
                scale.setToY(1.0);
                
                fade.play();
                scale.play();
            }
        }
    }
    
    private void selectTopicCard(TopicCard card, Topic topic) {
        // Deselect previous
        topicsContainer.getChildren().forEach(node -> {
            if (node instanceof TopicCard) {
                ((TopicCard) node).setSelected(false);
            }
        });
        
        // Select new
        card.setSelected(true);
        selectedTopic = topic;
        
        if (onTopicSelected != null) {
            onTopicSelected.accept(topic);
        }
    }
    
    public void showCreateTopicDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Create New Topic");
        dialog.setHeaderText("Enter a title for your new topic");
        dialog.getDialogPane().getStyleClass().add("create-topic-dialog");
        
        // Set the button types
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        
        // Create the title input field
        TextField titleField = new TextField();
        titleField.setPromptText("Topic title");
        titleField.getStyleClass().add("topic-title-field");
        titleField.setPrefWidth(350);
        
        VBox content = new VBox(10);
        content.getChildren().add(titleField);
        content.setPadding(new Insets(20));
        
        dialog.getDialogPane().setContent(content);
        
        // Enable/disable create button
        javafx.scene.Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);
        
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(newValue.trim().isEmpty());
        });
        
        // Focus on title field
        Platform.runLater(titleField::requestFocus);
        
        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return titleField.getText().trim();
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(title -> {
            createTopic(title);
        });
    }
    
    private void createTopic(String title) {
        showLoading(true);
        
        new Thread(() -> {
            try {
                Topic newTopic = apiService.createTopic(title);
                Platform.runLater(() -> {
                    showLoading(false);
                    refreshTopics();
                    if (onTopicCreated != null) {
                        onTopicCreated.accept(newTopic);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Failed to create topic: " + e.getMessage());
                    showLoading(false);
                });
            }
        }).start();
    }
    
    public void refreshTopics() {
        loadTopics();
    }
    
    public void selectTopic(Topic topic) {
        // Find and select the topic card
        for (javafx.scene.Node node : topicsContainer.getChildren()) {
            if (node instanceof TopicCard) {
                TopicCard card = (TopicCard) node;
                if (card.getTopic().getId() == topic.getId()) {
                    selectTopicCard(card, topic);
                    break;
                }
            }
        }
    }
    
    private void showLoading(boolean show) {
        loadingIndicator.setVisible(show);
    }
    
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        statusLabel.getStyleClass().add("error-label");
    }
    
    public void setOnTopicSelected(Consumer<Topic> handler) {
        this.onTopicSelected = handler;
    }
    
    public void setOnTopicCreated(Consumer<Topic> handler) {
        this.onTopicCreated = handler;
    }
    
    // Inner class for Topic Card
    private static class TopicCard extends VBox {
        private final Topic topic;
        private boolean selected = false;
        
        public TopicCard(Topic topic) {
            this.topic = topic;
            setupCard();
        }
        
        private void setupCard() {
            getStyleClass().add("topic-card");
            setPadding(new Insets(12, 15, 12, 15));
            setSpacing(5);
            
            Label titleLabel = new Label(topic.getTitle());
            titleLabel.getStyleClass().add("topic-card-title");
            titleLabel.setWrapText(true);
            
            Label countLabel = new Label(topic.getMessageCount() + " message" + 
                                        (topic.getMessageCount() != 1 ? "s" : ""));
            countLabel.getStyleClass().add("topic-card-count");
            
            getChildren().addAll(titleLabel, countLabel);
            
            // Hover effect
            setOnMouseEntered(e -> {
                if (!selected) {
                    getStyleClass().add("topic-card-hover");
                }
            });
            
            setOnMouseExited(e -> {
                if (!selected) {
                    getStyleClass().remove("topic-card-hover");
                }
            });
        }
        
        public void setSelected(boolean selected) {
            this.selected = selected;
            if (selected) {
                getStyleClass().add("topic-card-selected");
                getStyleClass().remove("topic-card-hover");
            } else {
                getStyleClass().remove("topic-card-selected");
            }
        }
        
        public Topic getTopic() {
            return topic;
        }
    }
}
