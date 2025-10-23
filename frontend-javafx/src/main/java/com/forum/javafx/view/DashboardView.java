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
import com.forum.javafx.service.SearchService;
import com.forum.javafx.model.SearchResult;
import com.forum.javafx.view.sections.*;
import javafx.collections.ObservableList;

public class DashboardView extends BorderPane {
    
    private AuthService authService;
    private SearchService searchService;
    private Runnable onLogout;
    
    // Top bar components
    private TextField searchField;
    private ListView<SearchResult> searchResultsList;
    private StackPane searchResultsPopup;
    private Button profileButton;
    private Label userNameLabel;
    
    // Navigation
    private ToggleGroup navigationGroup;
    private VBox navigationPane;
    
    // Content area
    private StackPane contentArea;
    
    // Sections
    private TopicsSection topicsSection;
    private ChatSection chatSection;
    private CalendarSection calendarSection;
    private SettingsSection settingsSection;
    private LibrarySection librarySection;
    private ProjectsSection projectsSection;
    private VBox dashboardHomeSection;
    
    public DashboardView() {
        this.authService = AuthService.getInstance();
        this.searchService = SearchService.getInstance();
        initialize();
        setupLayout();
        setupSearchFunctionality();
        playEntranceAnimation();
    }
    
    private void initialize() {
        getStyleClass().add("dashboard-view");
        
        dashboardHomeSection = createDashboardHome();
        topicsSection = new TopicsSection();
        chatSection = new ChatSection();
        calendarSection = new CalendarSection();
        settingsSection = new SettingsSection();
        librarySection = new LibrarySection();
        projectsSection = new ProjectsSection();
    }
    
    private void setupLayout() {
        // Top bar
        HBox topBar = createTopBar();
        
        // Wrap top bar in a StackPane to overlay search results
        StackPane topBarContainer = new StackPane();
        topBarContainer.getChildren().add(topBar);
        setTop(topBarContainer);
        
        // Left navigation
        setLeft(createNavigationPane());
        
        // Center content
        contentArea = new StackPane();
        contentArea.getStyleClass().add("content-area");
        setCenter(contentArea);
        
        // Show dashboard home by default
        showSection(dashboardHomeSection);
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.getStyleClass().add("dashboard-top-bar");
        topBar.setPadding(new Insets(15, 25, 15, 25));
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        // College logo and title
        HBox logoBox = new HBox(15);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        
        // Logo - use actual image (balanced size)
        ImageView logoView = new ImageView();
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/college-logo.png"));
            logoView.setImage(logoImage);
            logoView.setFitWidth(50);
            logoView.setFitHeight(50);
            logoView.setPreserveRatio(true);
            logoView.getStyleClass().add("college-logo");
            logoBox.getChildren().add(logoView);
        } catch (Exception e) {
            // Fallback to emoji if image not found
            Label logoLabel = new Label("🎓");
            logoLabel.getStyleClass().add("college-logo");
            logoLabel.setStyle("-fx-font-size: 40px;");
            logoBox.getChildren().add(logoLabel);
        }
        
        VBox titleBox = new VBox(2);
        Label titleLabel = new Label("Discussion Forum");
        titleLabel.getStyleClass().add("dashboard-title");
        titleLabel.setStyle("-fx-font-size: 18px;");
        Label collegeLabel = new Label("College Community");
        collegeLabel.getStyleClass().add("college-subtitle");
        collegeLabel.setStyle("-fx-font-size: 12px;");
        titleBox.getChildren().addAll(titleLabel, collegeLabel);
        
        logoBox.getChildren().add(titleBox);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Search bar
        VBox searchContainer = new VBox(0);
        searchContainer.setAlignment(Pos.TOP_LEFT);
        
        HBox searchBox = new HBox(5);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.getStyleClass().add("search-box");
        
        searchField = new TextField();
        searchField.setPromptText("🔍 Search messages, topics...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(300);
        
        searchBox.getChildren().add(searchField);
        
        // Search results dropdown
        searchResultsList = new ListView<>();
        searchResultsList.getStyleClass().add("search-results-list");
        searchResultsList.setPrefHeight(0);
        searchResultsList.setMaxHeight(250);
        searchResultsList.setVisible(false);
        searchResultsList.setManaged(false);
        
        // Custom cell factory for search results
        searchResultsList.setCellFactory(param -> new ListCell<SearchResult>() {
            @Override
            protected void updateItem(SearchResult item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    VBox resultBox = new VBox(3);
                    resultBox.setPadding(new Insets(8, 12, 8, 12));
                    
                    Label titleLabel = new Label(item.getTitle());
                    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
                    
                    Label sectionLabel = new Label(item.getSection() + " • " + item.getCategory());
                    sectionLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6C757D;");
                    
                    if (item.getDescription() != null && !item.getDescription().isEmpty()) {
                        Label descLabel = new Label(item.getDescription());
                        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #ADB5BD;");
                        descLabel.setWrapText(true);
                        resultBox.getChildren().addAll(titleLabel, sectionLabel, descLabel);
                    } else {
                        resultBox.getChildren().addAll(titleLabel, sectionLabel);
                    }
                    
                    setGraphic(resultBox);
                    setText(null);
                    setStyle("-fx-cursor: hand;");
                }
            }
        });
        
        searchContainer.getChildren().addAll(searchBox, searchResultsList);
        
        // User info
        VBox userBox = new VBox(2);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        
        userNameLabel = new Label(authService.getCurrentUser().getName());
        userNameLabel.getStyleClass().add("user-name-label");
        
        Label roleLabel = new Label(authService.getCurrentUser().getRole().toString());
        roleLabel.getStyleClass().add("user-role-label");
        
        userBox.getChildren().addAll(userNameLabel, roleLabel);
        
        // Profile button
        profileButton = new Button("👤");
        profileButton.getStyleClass().addAll("profile-button", "icon-button");
        profileButton.setTooltip(new Tooltip("Profile & Settings"));
        
        MenuButton profileMenu = new MenuButton("👤");
        profileMenu.getStyleClass().addAll("profile-menu-button", "icon-button");
        
        MenuItem profileItem = new MenuItem("Profile");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem logoutItem = new MenuItem("Logout");
        
        profileMenu.getItems().addAll(profileItem, settingsItem, new SeparatorMenuItem(), logoutItem);
        
        settingsItem.setOnAction(e -> showSection(settingsSection));
        logoutItem.setOnAction(e -> {
            if (onLogout != null) onLogout.run();
        });
        
        topBar.getChildren().addAll(logoBox, spacer, searchContainer, userBox, profileMenu);
        
        return topBar;
    }
    
    private VBox createNavigationPane() {
        VBox navPane = new VBox(5);
        navPane.getStyleClass().add("navigation-pane");
        navPane.setPadding(new Insets(20, 10, 20, 10));
        navPane.setPrefWidth(220);
        
        navigationGroup = new ToggleGroup();
        
        // Navigation buttons
        ToggleButton dashboardBtn = createNavButton("🏠 Dashboard", "dashboard");
        ToggleButton topicsBtn = createNavButton("💬 Topics", "topics");
        ToggleButton chatBtn = createNavButton("📨 Messages", "chat");
        ToggleButton calendarBtn = createNavButton("📅 Calendar", "calendar");
        ToggleButton libraryBtn = createNavButton("📚 Library", "library");
        ToggleButton projectsBtn = createNavButton("💼 Projects", "projects");
        
        dashboardBtn.setSelected(true);
        
        dashboardBtn.setOnAction(e -> showSection(dashboardHomeSection));
        topicsBtn.setOnAction(e -> showSection(topicsSection));
        chatBtn.setOnAction(e -> showSection(chatSection));
        calendarBtn.setOnAction(e -> showSection(calendarSection));
        libraryBtn.setOnAction(e -> showSection(librarySection));
        projectsBtn.setOnAction(e -> showSection(projectsSection));
        
        // Spacer
        Region navSpacer = new Region();
        VBox.setVgrow(navSpacer, Priority.ALWAYS);
        
        // Bottom buttons
        ToggleButton settingsBtn = createNavButton("⚙️ Settings", "settings");
        Label pluginsLabel = new Label("🔌 Plugins");
        pluginsLabel.getStyleClass().add("plugins-label");
        pluginsLabel.setPadding(new Insets(10, 15, 5, 15));
        
        settingsBtn.setOnAction(e -> showSection(settingsSection));
        
        navPane.getChildren().addAll(
            dashboardBtn, topicsBtn, chatBtn, calendarBtn, libraryBtn, projectsBtn, navSpacer, settingsBtn, pluginsLabel
        );
        
        return navPane;
    }
    
    private ToggleButton createNavButton(String text, String id) {
        ToggleButton btn = new ToggleButton(text);
        btn.getStyleClass().add("nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setToggleGroup(navigationGroup);
        btn.setId(id);
        return btn;
    }
    
    private void showSection(Region section) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), contentArea);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(section);
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contentArea);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
    
    private void playEntranceAnimation() {
        setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(600), this);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }
    
    private void setupSearchFunctionality() {
        // Update search results as user types
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                hideSearchResults();
                return;
            }
            
            ObservableList<SearchResult> results = searchService.search(newVal);
            
            if (results.isEmpty()) {
                hideSearchResults();
            } else {
                searchResultsList.setItems(results);
                showSearchResults();
            }
        });
        
        // Handle result selection
        searchResultsList.setOnMouseClicked(event -> {
            SearchResult selected = searchResultsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                navigateToSearchResult(selected);
                searchField.clear();
                hideSearchResults();
            }
        });
        
        // Hide results when clicking outside
        searchField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                // Delay hiding to allow click on result
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    hideSearchResults();
                });
            }
        });
    }
    
    private void showSearchResults() {
        searchResultsList.setVisible(true);
        searchResultsList.setManaged(true);
        searchResultsList.setPrefHeight(Math.min(searchResultsList.getItems().size() * 70 + 10, 250));
    }
    
    private void hideSearchResults() {
        searchResultsList.setVisible(false);
        searchResultsList.setManaged(false);
        searchResultsList.setPrefHeight(0);
    }
    
    private void navigateToSearchResult(SearchResult result) {
        String section = result.getSection();
        
        switch (section) {
            case "Topics":
                showSection(topicsSection);
                topicsSection.navigateToSearchResult(result);
                break;
            case "Messages":
                showSection(chatSection);
                break;
            case "Library":
                showSection(librarySection);
                librarySection.highlightResource(result);
                break;
            case "Projects":
                showSection(projectsSection);
                projectsSection.highlightProject(result);
                break;
        }
    }
    
    private VBox createDashboardHome() {
        VBox dashboard = new VBox(30);
        dashboard.setPadding(new Insets(35, 40, 35, 40));
        dashboard.getStyleClass().add("dashboard-home");
        
        // Welcome header
        Label welcomeLabel = new Label("Welcome Back!");
        welcomeLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #111827; -fx-padding: 0 0 5 0;");
        
        Label subtitleLabel = new Label("Here's what's happening with your forum today");
        subtitleLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #6B7280; -fx-padding: 0 0 10 0;");
        
        VBox header = new VBox(10);
        header.getChildren().addAll(welcomeLabel, subtitleLabel);
        
        // Statistics cards row with proper spacing
        HBox statsRow = new HBox(25);
        statsRow.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(statsRow, Priority.ALWAYS);
        
        VBox totalTopicsCard = createStatCard("💬", "Total Topics", "156", "+12 this week", "#6366F1");
        VBox activeUsersCard = createStatCard("👥", "Active Users", "1,247", "+23% from last month", "#10B981");
        VBox messagesCard = createStatCard("📨", "Messages", "8,394", "+156 today", "#F59E0B");
        VBox projectsCard = createStatCard("💼", "Projects", "42", "6 in progress", "#EC4899");
        
        HBox.setHgrow(totalTopicsCard, Priority.ALWAYS);
        HBox.setHgrow(activeUsersCard, Priority.ALWAYS);
        HBox.setHgrow(messagesCard, Priority.ALWAYS);
        HBox.setHgrow(projectsCard, Priority.ALWAYS);
        
        statsRow.getChildren().addAll(totalTopicsCard, activeUsersCard, messagesCard, projectsCard);
        
        // Main content grid with better spacing
        GridPane contentGrid = new GridPane();
        contentGrid.setHgap(25);
        contentGrid.setVgap(25);
        
        // Column constraints for proper sizing
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(55);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(45);
        contentGrid.getColumnConstraints().addAll(col1, col2);
        
        // Recent activity card
        VBox activityCard = createActivityCard();
        GridPane.setConstraints(activityCard, 0, 0);
        GridPane.setRowSpan(activityCard, 2);
        
        // Quick stats card
        VBox quickStatsCard = createQuickStatsCard();
        GridPane.setConstraints(quickStatsCard, 1, 0);
        
        // Trending topics card
        VBox trendingCard = createTrendingTopicsCard();
        GridPane.setConstraints(trendingCard, 1, 1);
        
        contentGrid.getChildren().addAll(activityCard, quickStatsCard, trendingCard);
        
        dashboard.getChildren().addAll(header, statsRow, contentGrid);
        
        return dashboard;
    }
    
    private VBox createStatCard(String icon, String title, String value, String subtitle, String color) {
        VBox card = new VBox(15);
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(280);
        card.setMinHeight(150);
        card.setPadding(new Insets(20, 25, 20, 25));
        card.setAlignment(Pos.TOP_LEFT);
        
        // Icon with better positioning
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 40px; -fx-padding: 0 0 8 0;");
        
        // Title with better spacing
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #6B7280; -fx-font-weight: 600; -fx-letter-spacing: 0.5px; -fx-padding: 0 0 5 0;");
        
        // Value with emphasis
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: " + color + "; -fx-padding: 0 0 8 0;");
        
        // Subtitle with subtle color
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #9CA3AF; -fx-font-weight: 500;");
        
        card.getChildren().addAll(iconLabel, titleLabel, valueLabel, subtitleLabel);
        
        return card;
    }
    
    private VBox createActivityCard() {
        VBox card = new VBox(18);
        card.getStyleClass().add("card");
        card.setPrefWidth(450);
        card.setMinHeight(420);
        card.setPadding(new Insets(25));
        
        Label title = new Label("Recent Activity");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #111827; -fx-padding: 0 0 10 0;");
        
        VBox activities = new VBox(14);
        activities.getChildren().addAll(
            createActivityItem("Sarah posted in Red Club", "2 minutes ago", "🛡️"),
            createActivityItem("New project 'AI Chatbot' created", "15 minutes ago", "💼"),
            createActivityItem("Michael joined Coding Club", "1 hour ago", "💻"),
            createActivityItem("Discussion on Web Security trending", "2 hours ago", "🔥"),
            createActivityItem("Library resource added: Clean Code", "3 hours ago", "📚")
        );
        
        card.getChildren().addAll(title, activities);
        
        return card;
    }
    
    private HBox createActivityItem(String text, String time, String icon) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(14, 16, 14, 16));
        item.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 12;");
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 22px; -fx-padding: 0 5 0 0;");
        iconLabel.setMinWidth(35);
        iconLabel.setAlignment(Pos.CENTER);
        
        VBox textBox = new VBox(5);
        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #111827; -fx-font-weight: 600;");
        Label timeLabel = new Label(time);
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #9CA3AF; -fx-font-weight: 500;");
        textBox.getChildren().addAll(textLabel, timeLabel);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        
        item.getChildren().addAll(iconLabel, textBox);
        
        return item;
    }
    
    private VBox createQuickStatsCard() {
        VBox card = new VBox(20);
        card.getStyleClass().add("gradient-card");
        card.setPrefWidth(380);
        card.setMinHeight(200);
        card.setPadding(new Insets(25));
        
        Label title = new Label("Quick Stats");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-padding: 0 0 12 0;");
        
        HBox statsRow1 = new HBox(40);
        statsRow1.setAlignment(Pos.CENTER_LEFT);
        statsRow1.getChildren().addAll(
            createMiniStat("32", "Projects", "#FFFFFF"),
            createMiniStat("156", "Topics", "#FFFFFF")
        );
        
        HBox statsRow2 = new HBox(40);
        statsRow2.setAlignment(Pos.CENTER_LEFT);
        statsRow2.getChildren().addAll(
            createMiniStat("1.2K", "Users", "#FFFFFF"),
            createMiniStat("8.4K", "Messages", "#FFFFFF")
        );
        
        card.getChildren().addAll(title, statsRow1, statsRow2);
        
        return card;
    }
    
    private VBox createMiniStat(String value, String label, String color) {
        VBox stat = new VBox(6);
        stat.setAlignment(Pos.CENTER_LEFT);
        stat.setMinWidth(120);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: " + color + "; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0, 0, 1);");
        
        Label labelText = new Label(label);
        labelText.setStyle("-fx-font-size: 13px; -fx-text-fill: #E5E7EB; -fx-font-weight: 600; -fx-letter-spacing: 0.3px;");
        
        stat.getChildren().addAll(valueLabel, labelText);
        
        return stat;
    }
    
    private VBox createTrendingTopicsCard() {
        VBox card = new VBox(18);
        card.getStyleClass().add("card");
        card.setPrefWidth(380);
        card.setMinHeight(200);
        card.setPadding(new Insets(25));
        
        Label title = new Label("🔥 Trending Topics");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #111827; -fx-padding: 0 0 10 0;");
        
        VBox topics = new VBox(12);
        topics.getChildren().addAll(
            createTrendingItem("Penetration Testing Basics", "45 replies"),
            createTrendingItem("React vs Angular 2025", "38 replies"),
            createTrendingItem("Machine Learning Project Ideas", "32 replies")
        );
        
        card.getChildren().addAll(title, topics);
        
        return card;
    }
    
    private HBox createTrendingItem(String topic, String replies) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 0, 10, 0));
        
        Label topicLabel = new Label(topic);
        topicLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #111827; -fx-font-weight: 600;");
        HBox.setHgrow(topicLabel, Priority.ALWAYS);
        
        Label repliesLabel = new Label(replies);
        repliesLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6B7280; -fx-background-color: #F3F4F6; -fx-padding: 6 12; -fx-background-radius: 8; -fx-font-weight: 600;");
        
        item.getChildren().addAll(topicLabel, repliesLabel);
        
        return item;
    }
    
    public void setOnLogout(Runnable handler) {
        this.onLogout = handler;
    }
}
