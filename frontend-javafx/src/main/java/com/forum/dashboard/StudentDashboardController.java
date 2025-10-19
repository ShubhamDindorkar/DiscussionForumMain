package com.forum.dashboard;

import javafx.animation.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StudentDashboardController {
    // Dashboard components
    @FXML private Text welcomeText;
    @FXML private TextField searchField;
    @FXML private Button notificationBtn;
    @FXML private Button profileBtn;
    @FXML private ListView<String> recentActivityList;
    
    // Feature cards
    @FXML private VBox topicsCard;
    @FXML private VBox myPostsCard;
    @FXML private VBox createPostCard;
    @FXML private VBox notificationsCard;
    @FXML private VBox profileCard;
    @FXML private VBox settingsCard;
    
    // Panes
    @FXML private VBox dashboardPane;
    @FXML private VBox topicsPane;
    @FXML private VBox myPostsPane;
    @FXML private VBox createPostPane;
    @FXML private VBox notificationsPane;
    @FXML private VBox profilePane;
    @FXML private VBox settingsPane;
    
    // Topics View
    @FXML private TableView<TopicItem> topicsTable;
    @FXML private TableColumn<TopicItem, String> topicTitleCol;
    @FXML private TableColumn<TopicItem, String> topicAuthorCol;
    @FXML private TableColumn<TopicItem, Integer> topicRepliesCol;
    @FXML private TableColumn<TopicItem, String> topicDateCol;
    @FXML private TableColumn<TopicItem, Void> topicActionsCol;
    
    // My Posts View
    @FXML private TableView<PostItem> myPostsTable;
    @FXML private TableColumn<PostItem, String> myPostTitleCol;
    @FXML private TableColumn<PostItem, Integer> myPostRepliesCol;
    @FXML private TableColumn<PostItem, String> myPostDateCol;
    @FXML private TableColumn<PostItem, String> myPostStatusCol;
    @FXML private TableColumn<PostItem, Void> myPostActionsCol;
    
    // Create Post View
    @FXML private TextField postTitleField;
    @FXML private TextArea postBodyField;
    @FXML private Button attachFileBtn;
    @FXML private Text attachedFileName;
    
    // Notifications View
    @FXML private ListView<String> notificationsList;
    
    // Profile View
    @FXML private TextField profileNameField;
    @FXML private TextField profileStudentIdField;
    @FXML private TextField profileEmailField;
    
    private String currentUsername;
    private File attachedFile;
    private ApiClient apiClient;
    
    @FXML
    public void initialize() {
        apiClient = new ApiClient();
        setupRecentActivity();
        setupTopicsTable();
        setupMyPostsTable();
        setupNotifications();
        
        // Add hover effects to cards
        addCardHoverEffect(topicsCard);
        addCardHoverEffect(myPostsCard);
        addCardHoverEffect(createPostCard);
        addCardHoverEffect(notificationsCard);
        addCardHoverEffect(profileCard);
        addCardHoverEffect(settingsCard);
    }
    
    public void setUsername(String username) {
        this.currentUsername = username;
        welcomeText.setText("Welcome back, " + username + "!");
        loadUserProfile();
    }
    
    private void setupRecentActivity() {
        ObservableList<String> activities = FXCollections.observableArrayList(
            "New reply on your post 'Java Basics'",
            "John Doe started following you",
            "Your post 'Study Tips' received 5 likes",
            "New topic in 'Programming' category",
            "Reminder: Assignment due tomorrow"
        );
        recentActivityList.setItems(activities);
    }
    
    private void setupTopicsTable() {
        topicTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        topicAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        topicRepliesCol.setCellValueFactory(new PropertyValueFactory<>("replies"));
        topicDateCol.setCellValueFactory(new PropertyValueFactory<>("lastActivity"));
        
        // Add action buttons
        topicActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            {
                viewBtn.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-padding: 6 12;");
                viewBtn.setOnAction(e -> {
                    TopicItem topic = getTableView().getItems().get(getIndex());
                    viewTopic(topic);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewBtn);
            }
        });
        
        loadTopics();
    }
    
    private void setupMyPostsTable() {
        myPostTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        myPostRepliesCol.setCellValueFactory(new PropertyValueFactory<>("replies"));
        myPostDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        myPostStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Add action buttons
        myPostActionsCol.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(8);
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            
            {
                editBtn.setStyle("-fx-background-color: #3182ce; -fx-text-fill: white; -fx-padding: 6 12;");
                deleteBtn.setStyle("-fx-background-color: #e53e3e; -fx-text-fill: white; -fx-padding: 6 12;");
                
                editBtn.setOnAction(e -> {
                    PostItem post = getTableView().getItems().get(getIndex());
                    editPost(post);
                });
                
                deleteBtn.setOnAction(e -> {
                    PostItem post = getTableView().getItems().get(getIndex());
                    deletePost(post);
                });
                
                buttons.getChildren().addAll(editBtn, deleteBtn);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        loadMyPosts();
    }
    
    private void setupNotifications() {
        ObservableList<String> notifications = FXCollections.observableArrayList(
            "🔔 New reply on 'JavaScript Arrays' - 2 min ago",
            "👤 Sarah Johnson mentioned you in a comment - 1 hour ago",
            "❤️ Your post received 10 likes - 3 hours ago",
            "📢 New announcement: Exam schedule released - 1 day ago",
            "✅ Your report was resolved - 2 days ago"
        );
        notificationsList.setItems(notifications);
    }
    
    private void loadTopics() {
        // Sample data - replace with API call
        ObservableList<TopicItem> topics = FXCollections.observableArrayList(
            new TopicItem("Introduction to Java Programming", "Alice Smith", 15, "2 hours ago"),
            new TopicItem("Data Structures Study Group", "Bob Johnson", 8, "5 hours ago"),
            new TopicItem("Web Development Tips", "Charlie Brown", 23, "1 day ago"),
            new TopicItem("Database Design Best Practices", "Diana Prince", 12, "2 days ago"),
            new TopicItem("Mobile App Development", "Ethan Hunt", 19, "3 days ago")
        );
        topicsTable.setItems(topics);
    }
    
    private void loadMyPosts() {
        // Sample data - replace with API call
        ObservableList<PostItem> posts = FXCollections.observableArrayList(
            new PostItem("My First Java Project", 5, "2024-10-15", "Active"),
            new PostItem("Help with Sorting Algorithms", 12, "2024-10-14", "Active"),
            new PostItem("React vs Angular Discussion", 8, "2024-10-10", "Closed")
        );
        myPostsTable.setItems(posts);
    }
    
    private void loadUserProfile() {
        // Sample data - replace with API call
        profileNameField.setText("John Student");
        profileStudentIdField.setText("S" + currentUsername.hashCode());
        profileEmailField.setText(currentUsername + "@university.edu");
    }
    
    @FXML
    private void showDashboard() {
        showPane(dashboardPane);
    }
    
    @FXML
    private void showTopics() {
        showPane(topicsPane);
        loadTopics();
    }
    
    @FXML
    private void showMyPosts() {
        showPane(myPostsPane);
        loadMyPosts();
    }
    
    @FXML
    private void showCreatePost() {
        showPane(createPostPane);
        postTitleField.clear();
        postBodyField.clear();
        attachedFileName.setText("");
        attachedFile = null;
    }
    
    @FXML
    private void showNotifications() {
        showPane(notificationsPane);
    }
    
    @FXML
    private void showProfile() {
        showPane(profilePane);
    }
    
    @FXML
    private void showSettings() {
        showPane(settingsPane);
    }
    
    @FXML
    private void handleAttachFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Attach File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Files", "*.*"),
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"),
            new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.doc", "*.docx")
        );
        
        attachedFile = fileChooser.showOpenDialog(attachFileBtn.getScene().getWindow());
        if (attachedFile != null) {
            attachedFileName.setText("📎 " + attachedFile.getName());
        }
    }
    
    @FXML
    private void handleCreatePost() {
        String title = postTitleField.getText().trim();
        String body = postBodyField.getText().trim();
        
        if (title.isEmpty() || body.isEmpty()) {
            showAlert("Please fill in all required fields", Alert.AlertType.WARNING);
            return;
        }
        
        // Create post via API
        // String json = String.format("{\"title\":\"%s\",\"body\":\"%s\"}", title, body);
        // apiClient.post("/topic", json);
        
        showAlert("Post created successfully!", Alert.AlertType.INFORMATION);
        showMyPosts();
    }
    
    @FXML
    private void handleSaveProfile() {
        showAlert("Profile updated successfully!", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void handleLogout() {
        MainApp.showStudentSignIn();
    }
    
    private void viewTopic(TopicItem topic) {
        showAlert("Opening topic: " + topic.getTitle(), Alert.AlertType.INFORMATION);
    }
    
    private void editPost(PostItem post) {
        postTitleField.setText(post.getTitle());
        postBodyField.setText("Edit your post content here...");
        showPane(createPostPane);
    }
    
    private void deletePost(PostItem post) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Post");
        alert.setHeaderText("Are you sure you want to delete this post?");
        alert.setContentText(post.getTitle());
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                myPostsTable.getItems().remove(post);
                showAlert("Post deleted successfully", Alert.AlertType.INFORMATION);
            }
        });
    }
    
    private void showPane(VBox targetPane) {
        dashboardPane.setVisible(false);
        topicsPane.setVisible(false);
        myPostsPane.setVisible(false);
        createPostPane.setVisible(false);
        notificationsPane.setVisible(false);
        profilePane.setVisible(false);
        settingsPane.setVisible(false);
        
        targetPane.setVisible(true);
        
        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.millis(300), targetPane);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }
    
    private void addCardHoverEffect(VBox card) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), card);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), card);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        
        card.setOnMouseEntered(e -> scaleUp.playFromStart());
        card.setOnMouseExited(e -> scaleDown.playFromStart());
    }
    
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Data model classes
    public static class TopicItem {
        private String title;
        private String author;
        private int replies;
        private String lastActivity;
        
        public TopicItem(String title, String author, int replies, String lastActivity) {
            this.title = title;
            this.author = author;
            this.replies = replies;
            this.lastActivity = lastActivity;
        }
        
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public int getReplies() { return replies; }
        public String getLastActivity() { return lastActivity; }
    }
    
    public static class PostItem {
        private String title;
        private int replies;
        private String date;
        private String status;
        
        public PostItem(String title, int replies, String date, String status) {
            this.title = title;
            this.replies = replies;
            this.date = date;
            this.status = status;
        }
        
        public String getTitle() { return title; }
        public int getReplies() { return replies; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }
}
