package com.forum.dashboard;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private BorderPane rootPane;
    @FXML private StackPane contentArea;
    @FXML private Button navOverview, navPosts, navUsers, navMessages, navReports;
    @FXML private Label titleLabel;
    @FXML private TextField searchField;
    @FXML private Button settingsButton, profileButton;

    // Overview pane
    @FXML private VBox overviewPane, projectsBox, calendarBox;
    @FXML private ListView<String> tasksListView, notificationsListView;

    // Posts pane
    @FXML private VBox postsPane;
    @FXML private Button createPostButton, refreshPosts;
    @FXML private TextField postSearchField;
    @FXML private TableView<PostItem> postsTableView;
    @FXML private TableColumn<PostItem, Integer> postIdCol;
    @FXML private TableColumn<PostItem, String> postTitleCol, postAuthorCol, postDateCol, postStatusCol;
    @FXML private TableColumn<PostItem, Void> postActionsCol;

    // Users pane
    @FXML private VBox usersPane;
    @FXML private Button addUserButton;
    @FXML private TextField userSearchField;
    @FXML private TableView<UserItem> usersTableView;
    @FXML private TableColumn<UserItem, Integer> userIdCol;
    @FXML private TableColumn<UserItem, String> usernameCol, emailCol, roleCol, statusCol;
    @FXML private TableColumn<UserItem, Void> userActionsCol;

    // Messages pane
    @FXML private VBox messagesPane;
    @FXML private TextField chatSearchField, messageInputField;
    @FXML private ListView<String> conversationsListView, messagesListView;
    @FXML private Label chatTitleLabel;
    @FXML private Button sendMessageButton;

    // Reports pane
    @FXML private VBox reportsPane;
    @FXML private Label pendingCountLabel;
    @FXML private TextField reportSearchField;
    @FXML private TableView<ReportItem> reportsTableView;
    @FXML private TableColumn<ReportItem, Integer> reportIdCol;
    @FXML private TableColumn<ReportItem, String> reportTypeCol, reportTargetCol, reportReasonCol, reportDateCol;
    @FXML private TableColumn<ReportItem, Void> reportActionsCol;

    private final ApiClient apiClient = new ApiClient();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupNavigation();
        setupTopBar();
        setupOverview();
        setupPosts();
        setupUsers();
        setupMessages();
        setupReports();
        showOverview();
    }

    private void setupNavigation() {
        navOverview.setOnAction(e -> showOverview());
        navPosts.setOnAction(e -> showPosts());
        navUsers.setOnAction(e -> showUsers());
        navMessages.setOnAction(e -> showMessages());
        navReports.setOnAction(e -> showReports());

        addHoverEffect(navOverview);
        addHoverEffect(navPosts);
        addHoverEffect(navUsers);
        addHoverEffect(navMessages);
        addHoverEffect(navReports);
    }

    private void addHoverEffect(Button b) {
        b.setOnMouseEntered(e -> b.getStyleClass().add("nav-hover"));
        b.setOnMouseExited(e -> b.getStyleClass().remove("nav-hover"));
    }

    private void setupTopBar() {
        profileButton.setOnAction(e -> showAlert("Profile", "Profile settings coming soon"));
        settingsButton.setOnAction(e -> showAlert("Settings", "Settings panel coming soon"));
    }

    private void setupOverview() {
        // Add project progress bars
        addProjectBar("Forum Redesign", 75);
        addProjectBar("Mobile App", 45);
        addProjectBar("API Documentation", 90);

        // Add tasks
        tasksListView.getItems().addAll(
            "✓ Review user reports",
            "○ Update content policy",
            "○ Schedule team meeting",
            "○ Analyze traffic metrics"
        );

        // Add notifications
        notificationsListView.getItems().addAll(
            "New user registration: john_doe",
            "Post flagged for review (ID: 234)",
            "System maintenance scheduled",
            "5 new messages in support"
        );
    }

    private void addProjectBar(String name, int progress) {
        VBox projectItem = new VBox(4);
        projectItem.getStyleClass().add("project-item");
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("project-name");
        
        HBox progressBox = new HBox(8);
        progressBox.setAlignment(Pos.CENTER_LEFT);
        ProgressBar bar = new ProgressBar(progress / 100.0);
        bar.setPrefWidth(200);
        bar.getStyleClass().add("project-progress");
        Label percentLabel = new Label(progress + "%");
        percentLabel.getStyleClass().add("project-percent");
        progressBox.getChildren().addAll(bar, percentLabel);
        
        projectItem.getChildren().addAll(nameLabel, progressBox);
        projectsBox.getChildren().add(projectItem);
    }

    private void setupPosts() {
        postIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        postTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        postAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        postDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        postStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        addButtonToTable(postActionsCol, "Delete", this::deletePost);

        createPostButton.setOnAction(e -> openCreatePostDialog());
        refreshPosts.setOnAction(e -> loadPosts());
    }

    private void loadPosts() {
        postsTableView.getItems().clear();
        // Sample data - in real app, call apiClient.get("/topic") and parse JSON
        postsTableView.getItems().addAll(
            new PostItem(1, "Welcome to the Forum", "admin", "2025-10-18 10:30", "Active"),
            new PostItem(2, "Exam Schedule Posted", "prof_smith", "2025-10-18 14:20", "Active"),
            new PostItem(3, "Lost and Found: Blue Backpack", "student123", "2025-10-19 08:15", "Active"),
            new PostItem(4, "JavaFX Tutorial Request", "dev_user", "2025-10-19 09:00", "Pinned")
        );
    }

    private void deletePost(PostItem post) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete post '" + post.getTitle() + "'?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                postsTableView.getItems().remove(post);
                // In real app: apiClient.delete("/topic/" + post.getId());
            }
        });
    }

    private void openCreatePostDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(rootPane.getScene().getWindow());
        dialog.setTitle("Create New Post");

        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("dialog-container");

        Label titleLabel = new Label("Create New Post");
        titleLabel.getStyleClass().add("dialog-title");

        TextField titleField = new TextField();
        titleField.setPromptText("Post title");
        titleField.getStyleClass().add("dialog-input");

        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Write your post content here...");
        bodyArea.setPrefRowCount(8);
        bodyArea.setWrapText(true);
        bodyArea.getStyleClass().add("dialog-textarea");

        HBox fileBox = new HBox(10);
        fileBox.setAlignment(Pos.CENTER_LEFT);
        Button attachBtn = new Button("📎 Attach File");
        attachBtn.getStyleClass().add("secondary-button");
        Label fileLabel = new Label("No file selected");
        fileLabel.getStyleClass().add("file-label");
        
        attachBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select File");
            File file = fc.showOpenDialog(dialog);
            if (file != null) {
                fileLabel.setText("📄 " + file.getName());
            }
        });
        fileBox.getChildren().addAll(attachBtn, fileLabel);

        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button submitBtn = new Button("Create Post");
        submitBtn.getStyleClass().add("primary-button");
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("secondary-button");

        submitBtn.setOnAction(e -> {
            if (!titleField.getText().isBlank()) {
                // In real app: apiClient.post("/topic", jsonPayload)
                postsTableView.getItems().add(0, new PostItem(
                    postsTableView.getItems().size() + 1,
                    titleField.getText(),
                    "current_user",
                    java.time.LocalDateTime.now().toString().substring(0, 16),
                    "Active"
                ));
                dialog.close();
            }
        });
        cancelBtn.setOnAction(e -> dialog.close());
        buttonBox.getChildren().addAll(cancelBtn, submitBtn);

        root.getChildren().addAll(titleLabel, titleField, bodyArea, fileBox, buttonBox);

        Scene scene = new Scene(root, 650, 500);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }

    private void setupUsers() {
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        addButtonToTable(userActionsCol, "Ban", this::banUser);

        addUserButton.setOnAction(e -> showAlert("Add User", "User creation coming soon"));
        loadUsers();
    }

    private void loadUsers() {
        usersTableView.getItems().clear();
        usersTableView.getItems().addAll(
            new UserItem(1, "admin", "admin@forum.com", "Admin", "Active"),
            new UserItem(2, "john_doe", "john@example.com", "User", "Active"),
            new UserItem(3, "jane_smith", "jane@example.com", "Moderator", "Active"),
            new UserItem(4, "spam_bot", "spam@bad.com", "User", "Banned")
        );
    }

    private void banUser(UserItem user) {
        if ("Banned".equals(user.getStatus())) {
            user.setStatus("Active");
        } else {
            user.setStatus("Banned");
        }
        usersTableView.refresh();
    }

    private void setupMessages() {
        conversationsListView.getItems().addAll(
            "👤 John Doe",
            "👤 Jane Smith",
            "👥 Support Team",
            "👤 Prof. Johnson"
        );

        conversationsListView.setOnMouseClicked(e -> {
            String selected = conversationsListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                chatTitleLabel.setText(selected);
                loadMessages(selected);
            }
        });

        sendMessageButton.setOnAction(e -> {
            String msg = messageInputField.getText();
            if (!msg.isBlank()) {
                messagesListView.getItems().add("You: " + msg);
                messageInputField.clear();
            }
        });
    }

    private void loadMessages(String conversation) {
        messagesListView.getItems().clear();
        messagesListView.getItems().addAll(
            conversation + ": Hello! How can I help you?",
            "You: I need help with my account",
            conversation + ": Sure, what seems to be the problem?",
            "You: I can't upload my profile picture"
        );
    }

    private void setupReports() {
        reportIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        reportTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportTargetCol.setCellValueFactory(new PropertyValueFactory<>("target"));
        reportReasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        reportDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        addButtonToTable(reportActionsCol, "Resolve", this::resolveReport);
        loadReports();
    }

    private void loadReports() {
        reportsTableView.getItems().clear();
        reportsTableView.getItems().addAll(
            new ReportItem(1, "Post", "Offensive content in post #234", "Harassment", "2025-10-19 08:30"),
            new ReportItem(2, "User", "Spam from user spam_bot", "Spam", "2025-10-19 09:15"),
            new ReportItem(3, "Post", "Inappropriate image", "NSFW", "2025-10-19 09:45")
        );
    }

    private void resolveReport(ReportItem report) {
        reportsTableView.getItems().remove(report);
        int pending = reportsTableView.getItems().size();
        pendingCountLabel.setText(String.valueOf(pending));
    }

    private <T> void addButtonToTable(TableColumn<T, Void> column, String label, java.util.function.Consumer<T> action) {
        column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button(label);
            {
                btn.getStyleClass().add("table-action-button");
                btn.setOnAction(e -> {
                    T item = getTableView().getItems().get(getIndex());
                    action.accept(item);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void showOverview() {
        titleLabel.setText("Overview");
        showPane(overviewPane);
    }

    private void showPosts() {
        titleLabel.setText("Posts & Threads");
        showPane(postsPane);
        loadPosts();
    }

    private void showUsers() {
        titleLabel.setText("Users");
        showPane(usersPane);
    }

    private void showMessages() {
        titleLabel.setText("Messages");
        showPane(messagesPane);
    }

    private void showReports() {
        titleLabel.setText("Moderation Queue");
        showPane(reportsPane);
    }

    private void showPane(Pane pane) {
        overviewPane.setVisible(false); overviewPane.setManaged(false);
        postsPane.setVisible(false); postsPane.setManaged(false);
        usersPane.setVisible(false); usersPane.setManaged(false);
        messagesPane.setVisible(false); messagesPane.setManaged(false);
        reportsPane.setVisible(false); reportsPane.setManaged(false);

        pane.setVisible(true); pane.setManaged(true);
        animatePane(pane);
    }

    private void animatePane(Pane pane) {
        FadeTransition fade = new FadeTransition(Duration.millis(250), pane);
        fade.setFromValue(0.5);
        fade.setToValue(1.0);
        
        TranslateTransition slide = new TranslateTransition(Duration.millis(250), pane);
        slide.setFromX(15);
        slide.setToX(0);
        
        fade.play();
        slide.play();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    // Data model classes
    public static class PostItem {
        private int id;
        private String title, author, date, status;
        
        public PostItem(int id, String title, String author, String date, String status) {
            this.id = id; this.title = title; this.author = author; 
            this.date = date; this.status = status;
        }
        
        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }

    public static class UserItem {
        private int id;
        private String username, email, role, status;
        
        public UserItem(int id, String username, String email, String role, String status) {
            this.id = id; this.username = username; this.email = email;
            this.role = role; this.status = status;
        }
        
        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class ReportItem {
        private int id;
        private String type, target, reason, date;
        
        public ReportItem(int id, String type, String target, String reason, String date) {
            this.id = id; this.type = type; this.target = target;
            this.reason = reason; this.date = date;
        }
        
        public int getId() { return id; }
        public String getType() { return type; }
        public String getTarget() { return target; }
        public String getReason() { return reason; }
        public String getDate() { return date; }
    }
}
