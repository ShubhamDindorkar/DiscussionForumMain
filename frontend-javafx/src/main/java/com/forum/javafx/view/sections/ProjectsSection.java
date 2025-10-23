package com.forum.javafx.view.sections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import com.forum.javafx.model.Project;
import com.forum.javafx.model.SearchResult;
import com.forum.javafx.service.SearchService;
import java.util.ArrayList;
import java.util.List;

public class ProjectsSection extends BorderPane {
    private FlowPane projectsGrid;
    private List<Project> projects;
    private ComboBox<String> statusFilter;
    private ComboBox<String> sortCombo;
    private SearchService searchService;
    
    public ProjectsSection() {
        projects = new ArrayList<>();
        searchService = SearchService.getInstance();
        setupUI();
        loadSampleProjects();
        populateSearchService();
    }
    
    private void setupUI() {
        getStyleClass().add("projects-section");
        
        // Top toolbar
        HBox toolbar = new HBox(15);
        toolbar.getStyleClass().add("section-toolbar");
        toolbar.setPadding(new Insets(15));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("💼 Current Projects");
        titleLabel.getStyleClass().add("section-header");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Projects", "Ongoing", "Completed", "Paused");
        statusFilter.setValue("All Projects");
        statusFilter.setOnAction(e -> filterAndSortProjects());
        
        sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll("Sort: Name (A-Z)", "Sort: Name (Z-A)", "Sort: Progress (High)", "Sort: Progress (Low)", "Sort: Status");
        sortCombo.setValue("Sort: Name (A-Z)");
        sortCombo.setOnAction(e -> filterAndSortProjects());
        
        Button addBtn = new Button("+ New Project");
        addBtn.getStyleClass().add("primary-button");
        addBtn.setOnAction(e -> showAddProjectDialog());
        
        toolbar.getChildren().addAll(titleLabel, spacer, statusFilter, sortCombo, addBtn);
        
        // Projects grid
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        projectsGrid = new FlowPane();
        projectsGrid.getStyleClass().add("projects-grid");
        projectsGrid.setHgap(20);
        projectsGrid.setVgap(20);
        projectsGrid.setPadding(new Insets(20));
        
        scrollPane.setContent(projectsGrid);
        
        setTop(toolbar);
        setCenter(scrollPane);
    }
    
    private void loadSampleProjects() {
        projects.add(createProject("1", "Discussion Forum", "Full-stack forum application with JavaFX frontend",
            "ONGOING", 75, "Java"));
        projects.add(createProject("2", "E-Commerce Platform", "Online shopping platform with payment integration",
            "ONGOING", 45, "React"));
        projects.add(createProject("3", "Weather App", "Real-time weather tracking application",
            "COMPLETED", 100, "Python"));
        projects.add(createProject("4", "Task Manager", "Team collaboration and task management tool",
            "ONGOING", 60, "Angular"));
        projects.add(createProject("5", "Chat Application", "Real-time messaging app with WebSocket",
            "PAUSED", 30, "Node.js"));
        projects.add(createProject("6", "Portfolio Website", "Personal portfolio with blog functionality",
            "COMPLETED", 100, "HTML/CSS"));
        
        updateProjectsGrid();
    }
    
    private Project createProject(String id, String name, String description, 
                                 String status, int progress, String language) {
        Project project = new Project(id, name, description, status, progress);
        project.setLanguage(language);
        project.setStartDate(java.time.LocalDate.now().minusDays(30).toString());
        project.setDueDate(java.time.LocalDate.now().plusDays(15).toString());
        return project;
    }
    
    private void filterAndSortProjects() {
        projectsGrid.getChildren().clear();
        String filter = statusFilter.getValue();
        String sortOption = sortCombo.getValue();
        
        // Filter projects
        List<Project> filteredProjects = new ArrayList<>();
        for (Project project : projects) {
            if (filter.equals("All Projects") || project.getStatus().equals(filter.toUpperCase())) {
                filteredProjects.add(project);
            }
        }
        
        // Sort projects
        if (sortOption.equals("Sort: Name (A-Z)")) {
            filteredProjects.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
        } else if (sortOption.equals("Sort: Name (Z-A)")) {
            filteredProjects.sort((p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
        } else if (sortOption.equals("Sort: Progress (High)")) {
            filteredProjects.sort((p1, p2) -> Integer.compare(p2.getProgress(), p1.getProgress()));
        } else if (sortOption.equals("Sort: Progress (Low)")) {
            filteredProjects.sort((p1, p2) -> Integer.compare(p1.getProgress(), p2.getProgress()));
        } else if (sortOption.equals("Sort: Status")) {
            filteredProjects.sort((p1, p2) -> p1.getStatus().compareTo(p2.getStatus()));
        }
        
        // Display filtered and sorted projects
        for (Project project : filteredProjects) {
            projectsGrid.getChildren().add(createProjectCard(project));
        }
    }
    
    private void updateProjectsGrid() {
        filterAndSortProjects();
    }
    
    private VBox createProjectCard(Project project) {
        VBox card = new VBox(12);
        card.getStyleClass().add("project-card");
        card.setPrefWidth(320);
        card.setMinHeight(240);
        
        // Header with status badge
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(project.getName());
        nameLabel.getStyleClass().add("project-name");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        
        Label statusBadge = new Label(project.getStatus());
        statusBadge.getStyleClass().add("status-badge");
        statusBadge.getStyleClass().add("status-" + project.getStatus().toLowerCase());
        
        header.getChildren().addAll(nameLabel, statusBadge);
        
        // Description
        Label descLabel = new Label(project.getDescription());
        descLabel.setWrapText(true);
        descLabel.getStyleClass().add("project-description");
        descLabel.setMaxHeight(60);
        
        // Language/Tech stack
        HBox techBox = new HBox(8);
        techBox.setAlignment(Pos.CENTER_LEFT);
        Label techIcon = new Label("💻");
        Label techLabel = new Label(project.getLanguage());
        techLabel.getStyleClass().add("project-tech");
        techBox.getChildren().addAll(techIcon, techLabel);
        
        // Progress with interactive slider
        VBox progressBox = new VBox(5);
        HBox progressHeader = new HBox();
        progressHeader.setAlignment(Pos.CENTER_LEFT);
        Label progressLabel = new Label("Progress");
        progressLabel.getStyleClass().add("progress-label");
        Region progressSpacer = new Region();
        HBox.setHgrow(progressSpacer, Priority.ALWAYS);
        Label percentLabel = new Label(project.getProgress() + "%");
        percentLabel.getStyleClass().add("progress-percent");
        progressHeader.getChildren().addAll(progressLabel, progressSpacer, percentLabel);
        
        ProgressBar progressBar = new ProgressBar(project.getProgress() / 100.0);
        progressBar.getStyleClass().add("project-progress");
        progressBar.setPrefWidth(280);
        
        progressBox.getChildren().addAll(progressHeader, progressBar);
        
        // Full action buttons row
        HBox actions = new HBox(6);
        actions.setAlignment(Pos.CENTER);
        
        Button viewBtn = new Button("👁");
        viewBtn.getStyleClass().addAll("icon-button-small");
        viewBtn.setTooltip(new Tooltip("View Details"));
        viewBtn.setOnAction(e -> showProjectDetails(project));
        
        Button editBtn = new Button("✏️");
        editBtn.getStyleClass().addAll("icon-button-small");
        editBtn.setTooltip(new Tooltip("Edit Project"));
        editBtn.setOnAction(e -> showEditProjectDialog(project));
        
        Button progressBtn = new Button("📊");
        progressBtn.getStyleClass().addAll("icon-button-small");
        progressBtn.setTooltip(new Tooltip("Update Progress"));
        progressBtn.setOnAction(e -> showUpdateProgressDialog(project));
        
        Button statusBtn = new Button("🔄");
        statusBtn.getStyleClass().addAll("icon-button-small");
        statusBtn.setTooltip(new Tooltip("Change Status"));
        statusBtn.setOnAction(e -> showChangeStatusDialog(project));
        
        Button deleteBtn = new Button("🗑️");
        deleteBtn.getStyleClass().addAll("icon-button-small", "danger-button");
        deleteBtn.setTooltip(new Tooltip("Delete Project"));
        deleteBtn.setOnAction(e -> confirmDeleteProject(project));
        
        actions.getChildren().addAll(viewBtn, editBtn, progressBtn, statusBtn, deleteBtn);
        
        card.getChildren().addAll(header, descLabel, techBox, progressBox, actions);
        
        return card;
    }
    
    private void showProjectDetails(Project project) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Project Details");
        dialog.setHeaderText(project.getName());
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-font-size: 13px;");
        
        content.getChildren().addAll(
            new Label("📋 Description: " + project.getDescription()),
            new Label("📊 Status: " + project.getStatus()),
            new Label("💻 Technology: " + project.getLanguage()),
            new Label("⏱️ Progress: " + project.getProgress() + "%"),
            new Label("📅 Start Date: " + project.getStartDate()),
            new Label("🎯 Due Date: " + project.getDueDate()),
            new Label("🆔 Project ID: " + project.getId())
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
    
    private void showEditProjectDialog(Project project) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit Project");
        dialog.setHeaderText("Edit " + project.getName());
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField(project.getName());
        TextArea descArea = new TextArea(project.getDescription());
        descArea.setPrefRowCount(3);
        TextField languageField = new TextField(project.getLanguage());
        TextField startDateField = new TextField(project.getStartDate());
        TextField dueDateField = new TextField(project.getDueDate());
        
        grid.add(new Label("Project Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descArea, 1, 1);
        grid.add(new Label("Language/Tech:"), 0, 2);
        grid.add(languageField, 1, 2);
        grid.add(new Label("Start Date:"), 0, 3);
        grid.add(startDateField, 1, 3);
        grid.add(new Label("Due Date:"), 0, 4);
        grid.add(dueDateField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                project.setName(nameField.getText());
                project.setDescription(descArea.getText());
                project.setLanguage(languageField.getText());
                project.setStartDate(startDateField.getText());
                project.setDueDate(dueDateField.getText());
                updateProjectsGrid();
                populateSearchService();
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showUpdateProgressDialog(Project project) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Update Progress");
        dialog.setHeaderText(project.getName() + " - Current: " + project.getProgress() + "%");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label label = new Label("Set new progress percentage:");
        
        Slider progressSlider = new Slider(0, 100, project.getProgress());
        progressSlider.setShowTickLabels(true);
        progressSlider.setShowTickMarks(true);
        progressSlider.setMajorTickUnit(25);
        progressSlider.setMinorTickCount(5);
        progressSlider.setBlockIncrement(5);
        
        Label valueLabel = new Label(String.format("%.0f%%", progressSlider.getValue()));
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        progressSlider.valueProperty().addListener((obs, old, newVal) -> {
            valueLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
        });
        
        content.getChildren().addAll(label, progressSlider, valueLabel);
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                project.setProgress((int) progressSlider.getValue());
                updateProjectsGrid();
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showChangeStatusDialog(Project project) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Change Status");
        dialog.setHeaderText(project.getName() + " - Current: " + project.getStatus());
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label label = new Label("Select new status:");
        
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("ONGOING", "COMPLETED", "PAUSED");
        statusCombo.setValue(project.getStatus());
        statusCombo.setPrefWidth(200);
        
        content.getChildren().addAll(label, statusCombo);
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                project.setStatus(statusCombo.getValue());
                updateProjectsGrid();
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void confirmDeleteProject(Project project) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Project");
        alert.setHeaderText("Delete " + project.getName() + "?");
        alert.setContentText("This action cannot be undone. Are you sure you want to delete this project?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                projects.remove(project);
                updateProjectsGrid();
                populateSearchService();
            }
        });
    }
    
    private void showAddProjectDialog() {
        Dialog<Project> dialog = new Dialog<>();
        dialog.setTitle("New Project");
        dialog.setHeaderText("Create a new project");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        TextArea descArea = new TextArea();
        descArea.setPrefRowCount(3);
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("ONGOING", "COMPLETED", "PAUSED");
        statusCombo.setValue("ONGOING");
        TextField languageField = new TextField();
        Slider progressSlider = new Slider(0, 100, 0);
        progressSlider.setShowTickLabels(true);
        progressSlider.setShowTickMarks(true);
        progressSlider.setMajorTickUnit(25);
        
        grid.add(new Label("Project Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descArea, 1, 1);
        grid.add(new Label("Status:"), 0, 2);
        grid.add(statusCombo, 1, 2);
        grid.add(new Label("Language/Tech:"), 0, 3);
        grid.add(languageField, 1, 3);
        grid.add(new Label("Progress:"), 0, 4);
        grid.add(progressSlider, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && !nameField.getText().isEmpty()) {
                String newId = String.valueOf(projects.size() + 1);
                Project newProject = createProject(newId, nameField.getText(), descArea.getText(),
                    statusCombo.getValue(), (int)progressSlider.getValue(), languageField.getText());
                projects.add(newProject);
                updateProjectsGrid();
                return newProject;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    /**
     * Populate search service with projects
     */
    private void populateSearchService() {
        searchService.setProjects(projects);
    }
    
    /**
     * Highlight a specific project from search
     */
    public void highlightProject(SearchResult result) {
        Object data = result.getData();
        if (data instanceof Project) {
            Project project = (Project) data;
            // Flash the project card or scroll to it
            System.out.println("Highlighting project: " + project.getName());
        }
    }
}
