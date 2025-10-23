package com.forum.javafx.view.sections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import com.forum.javafx.model.Club;
import com.forum.javafx.model.Topic;
import com.forum.javafx.model.Message;
import com.forum.javafx.model.SearchResult;
import com.forum.javafx.service.AuthService;
import com.forum.javafx.service.SearchService;
import java.util.*;

public class TopicsSection extends BorderPane {
    private FlowPane clubsGrid;
    private VBox discussionArea;
    private List<Club> clubs;
    private Club selectedClub;
    private ListView<Topic> topicsList;
    private VBox messagesArea;
    private Topic selectedTopic;
    private Map<String, List<Topic>> clubTopics;
    private Map<Long, List<Message>> topicMessages;
    private long topicIdCounter = 1;
    private long messageIdCounter = 1;
    private SearchService searchService;
    
    public TopicsSection() {
        clubs = new ArrayList<>();
        clubTopics = new HashMap<>();
        topicMessages = new HashMap<>();
        searchService = SearchService.getInstance();
        setupUI();
        loadCollegeClubs();
        populateSearchService();
    }
    
    private void setupUI() {
        getStyleClass().add("topics-section");
        
        // Left panel: Clubs
        VBox leftPanel = new VBox(20);
        leftPanel.getStyleClass().add("clubs-panel");
        leftPanel.setPrefWidth(280);
        leftPanel.setPadding(new Insets(20));
        
        Label clubsLabel = new Label("📚 College Clubs");
        clubsLabel.getStyleClass().add("section-header");
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        clubsGrid = new FlowPane();
        clubsGrid.getStyleClass().add("clubs-grid");
        clubsGrid.setVgap(15);
        clubsGrid.setHgap(15);
        clubsGrid.setPrefWrapLength(250);
        
        scrollPane.setContent(clubsGrid);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        leftPanel.getChildren().addAll(clubsLabel, scrollPane);
        
        // Right panel: Discussions
        discussionArea = new VBox(15);
        discussionArea.getStyleClass().add("discussion-area");
        discussionArea.setPadding(new Insets(20));
        
        showWelcomeMessage();
        
        setLeft(leftPanel);
        setCenter(discussionArea);
    }
    
    private void loadCollegeClubs() {
        // Create 4 college clubs
        Club cyberSecurity = createClub("1", "🛡️ Red Club (Cyber Security)", 
            "Cyber security, ethical hacking, and information security discussions",
            "#E74C3C", 156, 24);
        
        Club codingClub = createClub("2", "💻 Coding Club",
            "Programming, algorithms, competitive coding, and development",
            "#3498DB", 203, 45);
        
        Club aiMl = createClub("3", "🤖 AI & ML Club",
            "Artificial Intelligence, Machine Learning, and Data Science",
            "#9B59B6", 128, 18);
        
        Club webDev = createClub("4", "🌐 Web Development Club",
            "Web technologies, frameworks, and full-stack development",
            "#27AE60", 174, 31);
        
        clubs.addAll(Arrays.asList(cyberSecurity, codingClub, aiMl, webDev));
        
        // Create sample topics for each club
        createClubTopics();
        
        // Display clubs
        for (Club club : clubs) {
            clubsGrid.getChildren().add(createClubCard(club));
        }
    }
    
    private Club createClub(String id, String name, String description, 
                           String color, int members, int topics) {
        Club club = new Club(id, name, description, color);
        club.setMemberCount(members);
        club.setTopicCount(topics);
        clubTopics.put(id, new ArrayList<>());
        return club;
    }
    
    private void createClubTopics() {
        // Red Club (Cyber Security) topics
        addTopic("1", "Introduction to Penetration Testing", "admin", 12, 45);
        addTopic("1", "Best practices for password security", "Sarah Anderson", 8, 23);
        addTopic("1", "Network Security Fundamentals", "Michael Chen", 15, 67);
        addTopic("1", "Web Application Security", "admin", 10, 34);
        
        // Coding Club topics
        addTopic("2", "Dynamic Programming Techniques", "Emma Wilson", 20, 89);
        addTopic("2", "Java vs Python: Performance Comparison", "James Rodriguez", 14, 56);
        addTopic("2", "Git Best Practices for Teams", "admin", 9, 41);
        addTopic("2", "Competitive Programming Tips", "Liam Johnson", 18, 72);
        
        // AI & ML Club topics
        addTopic("3", "Neural Networks Explained", "Olivia Brown", 22, 98);
        addTopic("3", "Introduction to TensorFlow", "admin", 16, 54);
        addTopic("3", "Data Preprocessing Techniques", "Ava Davis", 11, 37);
        addTopic("3", "Machine Learning Project Ideas", "Noah Martinez", 13, 45);
        
        // Web Development Club topics
        addTopic("4", "React vs Angular: Which to choose?", "Sophia Taylor", 19, 83);
        addTopic("4", "Building RESTful APIs with Spring Boot", "admin", 17, 61);
        addTopic("4", "Modern CSS Techniques", "Emma Wilson", 12, 39);
        addTopic("4", "Full Stack Project Showcase", "James Rodriguez", 21, 94);
    }
    
    private void addTopic(String clubId, String title, String author, int replies, int views) {
        Topic topic = new Topic();
        topic.setId(topicIdCounter++);
        topic.setTitle(title);
        topic.setAuthor(author);
        topic.setReplyCount(replies);
        topic.setViewCount(views);
        topic.setTimestamp(new Date());
        
        clubTopics.get(clubId).add(topic);
        topicMessages.put(topic.getId(), createSampleMessages(title, author));
    }
    
    private List<Message> createSampleMessages(String topicTitle, String author) {
        List<Message> messages = new ArrayList<>();
        
        Message firstMsg = new Message();
        firstMsg.setId(messageIdCounter++);
        firstMsg.setContent("Let's discuss " + topicTitle + ". What are your thoughts?");
        firstMsg.setAuthor(author);
        firstMsg.setTimestamp(new Date());
        messages.add(firstMsg);
        
        Message reply1 = new Message();
        reply1.setId(messageIdCounter++);
        reply1.setContent("Great topic! I have some experience with this.");
        reply1.setAuthor("Student A");
        reply1.setTimestamp(new Date());
        messages.add(reply1);
        
        Message reply2 = new Message();
        reply2.setId(messageIdCounter++);
        reply2.setContent("Could you share more details? I'm interested in learning.");
        reply2.setAuthor("Student B");
        reply2.setTimestamp(new Date());
        messages.add(reply2);
        
        return messages;
    }
    
    private VBox createClubCard(Club club) {
        VBox card = new VBox(12);
        card.getStyleClass().add("club-card");
        card.setPrefWidth(240);
        card.setMinHeight(140);
        card.setStyle("-fx-border-color: " + club.getColor() + "; -fx-border-width: 0 0 0 4;");
        card.setCursor(javafx.scene.Cursor.HAND);
        
        // Club name with icon
        Label nameLabel = new Label(club.getName());
        nameLabel.getStyleClass().add("club-name");
        nameLabel.setWrapText(true);
        
        // Description
        Label descLabel = new Label(club.getDescription());
        descLabel.getStyleClass().add("club-description");
        descLabel.setWrapText(true);
        descLabel.setMaxHeight(50);
        
        // Stats
        HBox statsBox = new HBox(15);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        
        Label membersLabel = new Label("👥 " + club.getMemberCount() + " members");
        membersLabel.getStyleClass().add("club-stats");
        
        Label topicsLabel = new Label("💬 " + club.getTopicCount() + " topics");
        topicsLabel.getStyleClass().add("club-stats");
        
        statsBox.getChildren().addAll(membersLabel, topicsLabel);
        
        card.getChildren().addAll(nameLabel, descLabel, statsBox);
        
        // Click to view club discussions
        card.setOnMouseClicked(e -> showClubDiscussions(club));
        
        return card;
    }
    
    private void showWelcomeMessage() {
        discussionArea.getChildren().clear();
        
        VBox welcomeBox = new VBox(20);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(50));
        
        Label icon = new Label("📚");
        icon.setStyle("-fx-font-size: 72px;");
        
        Label titleLabel = new Label("College Clubs & Discussions");
        titleLabel.getStyleClass().add("welcome-title");
        
        Label subtitleLabel = new Label("Select a club to view discussions and participate");
        subtitleLabel.getStyleClass().add("welcome-subtitle");
        
        welcomeBox.getChildren().addAll(icon, titleLabel, subtitleLabel);
        discussionArea.getChildren().add(welcomeBox);
    }
    
    private void showClubDiscussions(Club club) {
        selectedClub = club;
        discussionArea.getChildren().clear();
        
        // Header with sort option
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 15, 0));
        
        Button backBtn = new Button("← Back to Clubs");
        backBtn.getStyleClass().add("back-button");
        backBtn.setOnAction(e -> showWelcomeMessage());
        
        Label clubTitle = new Label(club.getName());
        clubTitle.getStyleClass().add("club-discussion-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        ComboBox<String> sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll("Sort: Recent", "Sort: Most Replies", "Sort: Most Views", "Sort: Title");
        sortCombo.setValue("Sort: Recent");
        sortCombo.setOnAction(e -> sortTopics(sortCombo.getValue()));
        
        Button newTopicBtn = new Button("+ New Topic");
        newTopicBtn.getStyleClass().add("primary-button");
        newTopicBtn.setOnAction(e -> showNewTopicDialog());
        
        header.getChildren().addAll(backBtn, clubTitle, spacer, sortCombo, newTopicBtn);
        
        // Topics list
        topicsList = new ListView<>();
        topicsList.getStyleClass().add("topics-list");
        topicsList.setCellFactory(lv -> new TopicCell());
        
        List<Topic> topics = clubTopics.get(club.getId());
        if (topics != null) {
            topicsList.getItems().addAll(topics);
        }
        
        topicsList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Topic selected = topicsList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showTopicMessages(selected);
                }
            }
        });
        
        VBox.setVgrow(topicsList, Priority.ALWAYS);
        
        discussionArea.getChildren().addAll(header, topicsList);
    }
    
    private void sortTopics(String sortOption) {
        if (topicsList == null || selectedClub == null) return;
        
        List<Topic> topics = new ArrayList<>(clubTopics.get(selectedClub.getId()));
        
        if (sortOption.equals("Sort: Most Replies")) {
            topics.sort((t1, t2) -> Integer.compare(t2.getReplyCount(), t1.getReplyCount()));
        } else if (sortOption.equals("Sort: Most Views")) {
            topics.sort((t1, t2) -> Integer.compare(t2.getViewCount(), t1.getViewCount()));
        } else if (sortOption.equals("Sort: Title")) {
            topics.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
        } else { // Recent (default order)
            // Keep original order or sort by timestamp if available
        }
        
        topicsList.getItems().setAll(topics);
    }
    
    private void showTopicMessages(Topic topic) {
        selectedTopic = topic;
        discussionArea.getChildren().clear();
        
        // Header
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        Button backBtn = new Button("← Back to " + selectedClub.getName());
        backBtn.getStyleClass().add("back-button");
        backBtn.setOnAction(e -> showClubDiscussions(selectedClub));
        
        VBox titleBox = new VBox(5);
        Label titleLabel = new Label(topic.getTitle());
        titleLabel.getStyleClass().add("topic-title");
        
        Label infoLabel = new Label("by " + topic.getAuthor() + " • " + topic.getReplyCount() + " replies • " + topic.getViewCount() + " views");
        infoLabel.getStyleClass().add("topic-info");
        
        titleBox.getChildren().addAll(titleLabel, infoLabel);
        HBox.setHgrow(titleBox, Priority.ALWAYS);
        
        header.getChildren().addAll(backBtn, titleBox);
        
        // Messages area
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("messages-scroll");
        
        messagesArea = new VBox(15);
        messagesArea.getStyleClass().add("topic-messages-area");
        messagesArea.setPadding(new Insets(15));
        
        List<Message> messages = topicMessages.get(topic.getId());
        if (messages != null) {
            for (Message msg : messages) {
                messagesArea.getChildren().add(createMessageCard(msg));
            }
        }
        
        scrollPane.setContent(messagesArea);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        // Reply area
        HBox replyArea = new HBox(10);
        replyArea.setPadding(new Insets(15, 0, 0, 0));
        replyArea.setAlignment(Pos.CENTER);
        
        TextArea replyField = new TextArea();
        replyField.setPromptText("Write your reply...");
        replyField.setPrefRowCount(2);
        replyField.setWrapText(true);
        HBox.setHgrow(replyField, Priority.ALWAYS);
        
        Button replyBtn = new Button("Post Reply");
        replyBtn.getStyleClass().add("primary-button");
        replyBtn.setOnAction(e -> {
            String replyText = replyField.getText().trim();
            if (!replyText.isEmpty()) {
                Message newMsg = new Message();
                newMsg.setId(messageIdCounter++);
                newMsg.setContent(replyText);
                newMsg.setAuthor(AuthService.getInstance().getCurrentUser().getName());
                newMsg.setTimestamp(new Date());
                
                messages.add(newMsg);
                messagesArea.getChildren().add(createMessageCard(newMsg));
                replyField.clear();
                
                topic.setReplyCount(topic.getReplyCount() + 1);
            }
        });
        
        replyArea.getChildren().addAll(replyField, replyBtn);
        
        discussionArea.getChildren().addAll(header, scrollPane, replyArea);
    }
    
    private VBox createMessageCard(Message msg) {
        VBox card = new VBox(10);
        card.getStyleClass().add("message-card");
        
        HBox authorBox = new HBox(10);
        authorBox.setAlignment(Pos.CENTER_LEFT);
        
        Label authorIcon = new Label("👤");
        Label authorLabel = new Label(msg.getAuthor());
        authorLabel.getStyleClass().add("message-author");
        
        Label timeLabel = new Label("• " + formatTime(msg.getTimestamp()));
        timeLabel.getStyleClass().add("message-time");
        
        authorBox.getChildren().addAll(authorIcon, authorLabel, timeLabel);
        
        Label contentLabel = new Label(msg.getContent());
        contentLabel.setWrapText(true);
        contentLabel.getStyleClass().add("message-content");
        
        card.getChildren().addAll(authorBox, contentLabel);
        
        return card;
    }
    
    private String formatTime(Date date) {
        long diff = new Date().getTime() - date.getTime();
        long minutes = diff / (60 * 1000);
        
        if (minutes < 1) return "just now";
        if (minutes < 60) return minutes + " minutes ago";
        
        long hours = minutes / 60;
        if (hours < 24) return hours + " hours ago";
        
        long days = hours / 24;
        return days + " days ago";
    }
    
    private void showNewTopicDialog() {
        Dialog<Topic> dialog = new Dialog<>();
        dialog.setTitle("New Discussion Topic");
        dialog.setHeaderText("Create a new topic in " + selectedClub.getName());
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField titleField = new TextField();
        titleField.setPromptText("Topic title");
        
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("What would you like to discuss?");
        contentArea.setPrefRowCount(4);
        
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Content:"), 0, 1);
        grid.add(contentArea, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && !titleField.getText().isEmpty()) {
                addTopic(selectedClub.getId(), titleField.getText(), 
                    AuthService.getInstance().getCurrentUser().getName(), 0, 1);
                showClubDiscussions(selectedClub);
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    // Custom cell for topics list
    private class TopicCell extends ListCell<Topic> {
        private VBox content;
        private Label titleLabel;
        private Label authorLabel;
        private Label statsLabel;
        
        public TopicCell() {
            super();
            
            content = new VBox(8);
            content.setPadding(new Insets(12));
            content.getStyleClass().add("topic-cell");
            
            titleLabel = new Label();
            titleLabel.getStyleClass().add("topic-cell-title");
            titleLabel.setWrapText(true);
            
            authorLabel = new Label();
            authorLabel.getStyleClass().add("topic-cell-author");
            
            statsLabel = new Label();
            statsLabel.getStyleClass().add("topic-cell-stats");
            
            content.getChildren().addAll(titleLabel, authorLabel, statsLabel);
        }
        
        @Override
        protected void updateItem(Topic topic, boolean empty) {
            super.updateItem(topic, empty);
            
            if (empty || topic == null) {
                setGraphic(null);
            } else {
                titleLabel.setText(topic.getTitle());
                authorLabel.setText("by " + topic.getAuthor());
                statsLabel.setText("💬 " + topic.getReplyCount() + " replies  •  👁 " + topic.getViewCount() + " views");
                
                setGraphic(content);
            }
        }
    }
    
    /**
     * Populate search service with all topics and messages
     */
    private void populateSearchService() {
        searchService.setClubs(clubs);
        
        List<Topic> allTopics = new ArrayList<>();
        List<Message> allMessages = new ArrayList<>();
        
        for (List<Topic> topics : clubTopics.values()) {
            allTopics.addAll(topics);
        }
        
        for (List<Message> messages : topicMessages.values()) {
            allMessages.addAll(messages);
        }
        
        searchService.setTopics(allTopics);
        searchService.setMessages(allMessages);
    }
    
    /**
     * Navigate to a search result
     */
    public void navigateToSearchResult(SearchResult result) {
        Object data = result.getData();
        
        if (data instanceof Club) {
            Club club = (Club) data;
            for (Club c : clubs) {
                if (c.getId().equals(club.getId())) {
                    showClubDiscussions(c);
                    break;
                }
            }
        } else if (data instanceof Topic) {
            Topic topic = (Topic) data;
            // Find which club this topic belongs to
            for (Map.Entry<String, List<Topic>> entry : clubTopics.entrySet()) {
                if (entry.getValue().contains(topic)) {
                    // Find the club
                    for (Club club : clubs) {
                        if (club.getId().equals(entry.getKey())) {
                            showClubDiscussions(club);
                            // Highlight the topic
                            topicsList.getSelectionModel().select(topic);
                            topicsList.scrollTo(topic);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
}
