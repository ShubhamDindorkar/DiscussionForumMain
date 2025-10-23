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
        
        // Show topics by default
        showSection(topicsSection);
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
        ToggleButton topicsBtn = createNavButton("💬 Topics", "topics");
        ToggleButton chatBtn = createNavButton("📨 Messages", "chat");
        ToggleButton calendarBtn = createNavButton("📅 Calendar", "calendar");
        ToggleButton libraryBtn = createNavButton("📚 Library", "library");
        ToggleButton projectsBtn = createNavButton("💼 Projects", "projects");
        
        topicsBtn.setSelected(true);
        
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
            topicsBtn, chatBtn, calendarBtn, libraryBtn, projectsBtn, navSpacer, settingsBtn, pluginsLabel
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
    
    public void setOnLogout(Runnable handler) {
        this.onLogout = handler;
    }
}
