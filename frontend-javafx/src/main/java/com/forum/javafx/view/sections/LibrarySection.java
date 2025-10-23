package com.forum.javafx.view.sections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.forum.javafx.model.LibraryResource;
import com.forum.javafx.model.SearchResult;
import com.forum.javafx.service.SearchService;
import java.util.ArrayList;
import java.util.List;

public class LibrarySection extends BorderPane {
    private ListView<LibraryResource> resourcesList;
    private List<LibraryResource> resources;
    private ComboBox<String> categoryFilter;
    private ComboBox<String> sortCombo;
    private SearchService searchService;
    
    public LibrarySection() {
        resources = new ArrayList<>();
        searchService = SearchService.getInstance();
        setupUI();
        loadSampleResources();
        populateSearchService();
    }
    
    private void setupUI() {
        getStyleClass().add("library-section");
        
        // Top toolbar
        HBox toolbar = new HBox(15);
        toolbar.getStyleClass().add("section-toolbar");
        toolbar.setPadding(new Insets(15));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("📚 Library");
        titleLabel.getStyleClass().add("section-header");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll("All Categories", "Books", "Papers", "Articles", "Videos", "Courses");
        categoryFilter.setValue("All Categories");
        categoryFilter.setOnAction(e -> filterAndSortResources());
        
        sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll("Sort: Title (A-Z)", "Sort: Title (Z-A)", "Sort: Author", "Sort: Category");
        sortCombo.setValue("Sort: Title (A-Z)");
        sortCombo.setOnAction(e -> filterAndSortResources());
        
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search resources...");
        searchField.setPrefWidth(200);
        searchField.textProperty().addListener((obs, old, newVal) -> filterAndSortResources());
        
        Button addBtn = new Button("+ Add Resource");
        addBtn.getStyleClass().add("primary-button");
        addBtn.setOnAction(e -> showAddResourceDialog());
        
        toolbar.getChildren().addAll(titleLabel, spacer, categoryFilter, sortCombo, searchField, addBtn);
        
        // Resources list
        resourcesList = new ListView<>();
        resourcesList.getStyleClass().add("resources-list");
        resourcesList.setCellFactory(lv -> new ResourceCell());
        
        setTop(toolbar);
        setCenter(resourcesList);
    }
    
    private void loadSampleResources() {
        resources.add(createResource("1", "Clean Code", "Robert C. Martin", "Books", 
            "A Handbook of Agile Software Craftsmanship", "https://example.com/clean-code"));
        resources.add(createResource("2", "Design Patterns", "Gang of Four", "Books",
            "Elements of Reusable Object-Oriented Software", "https://example.com/design-patterns"));
        resources.add(createResource("3", "Introduction to Algorithms", "CLRS", "Books",
            "Comprehensive guide to algorithms", "https://example.com/algorithms"));
        resources.add(createResource("4", "JavaFX Tutorial Series", "Oracle", "Videos",
            "Complete JavaFX video tutorials", "https://example.com/javafx"));
        resources.add(createResource("5", "Spring Boot Guide", "Baeldung", "Articles",
            "Complete guide to Spring Boot development", "https://example.com/spring"));
        resources.add(createResource("6", "Machine Learning Basics", "Andrew Ng", "Courses",
            "Introduction to Machine Learning", "https://example.com/ml"));
        resources.add(createResource("7", "Effective Java", "Joshua Bloch", "Books",
            "Best practices for Java programming", "https://example.com/effective-java"));
        resources.add(createResource("8", "Database Systems Paper", "Various Authors", "Papers",
            "Research on distributed database systems", "https://example.com/db-paper"));
        
        updateResourcesList();
    }
    
    private LibraryResource createResource(String id, String title, String author, 
                                          String category, String description, String url) {
        LibraryResource resource = new LibraryResource(id, title, author, category);
        resource.setDescription(description);
        resource.setUrl(url);
        resource.setDateAdded(java.time.LocalDate.now().toString());
        return resource;
    }
    
    private void filterResources() {
        filterAndSortResources();
    }
    
    private void filterAndSortResources() {
        String filterValue = categoryFilter.getValue();
        String sortOption = sortCombo.getValue();
        
        resourcesList.getItems().clear();
        
        // Filter resources
        List<LibraryResource> filtered = new ArrayList<>();
        for (LibraryResource resource : resources) {
            if (filterValue.equals("All Categories") || resource.getCategory().equals(filterValue)) {
                filtered.add(resource);
            }
        }
        
        // Sort resources
        if (sortOption.equals("Sort: Title (A-Z)")) {
            filtered.sort((r1, r2) -> r1.getTitle().compareToIgnoreCase(r2.getTitle()));
        } else if (sortOption.equals("Sort: Title (Z-A)")) {
            filtered.sort((r1, r2) -> r2.getTitle().compareToIgnoreCase(r1.getTitle()));
        } else if (sortOption.equals("Sort: Author")) {
            filtered.sort((r1, r2) -> r1.getAuthor().compareToIgnoreCase(r2.getAuthor()));
        } else if (sortOption.equals("Sort: Category")) {
            filtered.sort((r1, r2) -> r1.getCategory().compareTo(r2.getCategory()));
        }
        
        resourcesList.getItems().addAll(filtered);
    }
    
    private void updateResourcesList() {
        filterAndSortResources();
    }
    
    private void showAddResourceDialog() {
        Dialog<LibraryResource> dialog = new Dialog<>();
        dialog.setTitle("Add Library Resource");
        dialog.setHeaderText("Add a new resource to the library");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Book", "Paper", "Article", "Video", "Course");
        categoryCombo.setValue("Book");
        TextArea descArea = new TextArea();
        descArea.setPrefRowCount(3);
        TextField urlField = new TextField();
        
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryCombo, 1, 2);
        grid.add(new Label("Description:"), 0, 3);
        grid.add(descArea, 1, 3);
        grid.add(new Label("URL:"), 0, 4);
        grid.add(urlField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && !titleField.getText().isEmpty()) {
                String newId = String.valueOf(resources.size() + 1);
                LibraryResource newResource = createResource(newId, titleField.getText(),
                    authorField.getText(), categoryCombo.getValue(), descArea.getText(), urlField.getText());
                resources.add(newResource);
                updateResourcesList();
                return newResource;
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    // Custom cell for resource list
    private class ResourceCell extends ListCell<LibraryResource> {
        private VBox content;
        private Label titleLabel;
        private Label authorLabel;
        private Label descLabel;
        private Label categoryBadge;
        private Button openBtn;
        private Button favoriteBtn;
        
        public ResourceCell() {
            super();
            
            content = new VBox(8);
            content.setPadding(new Insets(12));
            content.getStyleClass().add("resource-card");
            
            HBox topRow = new HBox(10);
            topRow.setAlignment(Pos.CENTER_LEFT);
            
            titleLabel = new Label();
            titleLabel.getStyleClass().add("resource-title");
            HBox.setHgrow(titleLabel, Priority.ALWAYS);
            
            categoryBadge = new Label();
            categoryBadge.getStyleClass().add("category-badge");
            
            favoriteBtn = new Button("⭐");
            favoriteBtn.getStyleClass().add("icon-button-small");
            
            topRow.getChildren().addAll(titleLabel, categoryBadge, favoriteBtn);
            
            authorLabel = new Label();
            authorLabel.getStyleClass().add("resource-author");
            
            descLabel = new Label();
            descLabel.setWrapText(true);
            descLabel.getStyleClass().add("resource-description");
            descLabel.setMaxWidth(600);
            
            HBox bottomRow = new HBox(10);
            bottomRow.setAlignment(Pos.CENTER_LEFT);
            
            openBtn = new Button("📖 Open");
            openBtn.getStyleClass().add("secondary-button");
            
            bottomRow.getChildren().add(openBtn);
            
            content.getChildren().addAll(topRow, authorLabel, descLabel, bottomRow);
        }
        
        @Override
        protected void updateItem(LibraryResource resource, boolean empty) {
            super.updateItem(resource, empty);
            
            if (empty || resource == null) {
                setGraphic(null);
            } else {
                titleLabel.setText(resource.getTitle());
                authorLabel.setText("by " + resource.getAuthor());
                descLabel.setText(resource.getDescription());
                categoryBadge.setText(resource.getCategory());
                
                favoriteBtn.setText(resource.isFavorite() ? "⭐" : "☆");
                favoriteBtn.setOnAction(e -> {
                    resource.setFavorite(!resource.isFavorite());
                    favoriteBtn.setText(resource.isFavorite() ? "⭐" : "☆");
                });
                
                openBtn.setOnAction(e -> {
                    // Open resource URL
                    System.out.println("Opening: " + resource.getUrl());
                });
                
                setGraphic(content);
            }
        }
    }
    
    /**
     * Populate search service with library resources
     */
    private void populateSearchService() {
        searchService.setLibraryResources(resources);
    }
    
    /**
     * Highlight a specific resource from search
     */
    public void highlightResource(SearchResult result) {
        Object data = result.getData();
        if (data instanceof LibraryResource) {
            LibraryResource resource = (LibraryResource) data;
            resourcesList.getSelectionModel().select(resource);
            resourcesList.scrollTo(resource);
        }
    }
}
