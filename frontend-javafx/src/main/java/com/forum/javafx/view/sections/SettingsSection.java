package com.forum.javafx.view.sections;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.forum.javafx.service.AuthService;

public class SettingsSection extends VBox {
    
    private AuthService authService;
    
    public SettingsSection() {
        this.authService = AuthService.getInstance();
        initialize();
        setupLayout();
    }
    
    private void initialize() {
        getStyleClass().add("settings-section");
        setPadding(new Insets(30));
        setSpacing(20);
    }
    
    private void setupLayout() {
        Label titleLabel = new Label("⚙️ Settings");
        titleLabel.getStyleClass().add("settings-title");
        
        // Profile Section
        TitledPane profilePane = createProfileSection();
        
        // Notifications Section
        TitledPane notificationsPane = createNotificationsSection();
        
        // Appearance Section
        TitledPane appearancePane = createAppearanceSection();
        
        // Plugins Section
        TitledPane pluginsPane = createPluginsSection();
        
        Accordion accordion = new Accordion(profilePane, notificationsPane, appearancePane, pluginsPane);
        accordion.setExpandedPane(profilePane);
        
        getChildren().addAll(titleLabel, accordion);
    }
    
    private TitledPane createProfileSection() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        TextField nameField = new TextField(authService.getCurrentUser().getName());
        TextField emailField = new TextField(authService.getCurrentUser().getEmail());
        emailField.setDisable(true);
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(new Label(authService.getCurrentUser().getRole().toString()), 1, 2);
        
        Button saveBtn = new Button("Save Changes");
        saveBtn.getStyleClass().add("primary-button");
        
        content.getChildren().addAll(grid, saveBtn);
        
        return new TitledPane("👤 Profile Settings", content);
    }
    
    private TitledPane createNotificationsSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        
        CheckBox emailNotif = new CheckBox("Email notifications");
        CheckBox desktopNotif = new CheckBox("Desktop notifications");
        CheckBox messageNotif = new CheckBox("New message alerts");
        CheckBox topicNotif = new CheckBox("Topic updates");
        
        emailNotif.setSelected(true);
        desktopNotif.setSelected(true);
        messageNotif.setSelected(true);
        
        content.getChildren().addAll(emailNotif, desktopNotif, messageNotif, topicNotif);
        
        return new TitledPane("🔔 Notifications", content);
    }
    
    private TitledPane createAppearanceSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        
        ComboBox<String> themeBox = new ComboBox<>();
        themeBox.getItems().addAll("Light Theme", "Dark Theme", "Auto");
        themeBox.setValue("Light Theme");
        themeBox.setPrefWidth(200);
        
        ComboBox<String> fontBox = new ComboBox<>();
        fontBox.getItems().addAll("Small", "Medium", "Large");
        fontBox.setValue("Medium");
        fontBox.setPrefWidth(200);
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        grid.add(new Label("Theme:"), 0, 0);
        grid.add(themeBox, 1, 0);
        grid.add(new Label("Font Size:"), 0, 1);
        grid.add(fontBox, 1, 1);
        
        content.getChildren().add(grid);
        
        return new TitledPane("🎨 Appearance", content);
    }
    
    private TitledPane createPluginsSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        
        Label infoLabel = new Label("Manage extensions and plugins");
        infoLabel.getStyleClass().add("info-label");
        
        ListView<String> pluginsList = new ListView<>();
        pluginsList.getItems().addAll(
            "🔌 Code Editor Plugin - Enabled",
            "🔌 File Manager - Enabled",
            "🔌 Markdown Preview - Disabled",
            "🔌 Git Integration - Enabled"
        );
        pluginsList.setPrefHeight(150);
        
        Button addPluginBtn = new Button("+ Add Plugin");
        addPluginBtn.getStyleClass().add("secondary-button");
        
        content.getChildren().addAll(infoLabel, pluginsList, addPluginBtn);
        
        return new TitledPane("🔌 Plugins & Extensions", content);
    }
}
