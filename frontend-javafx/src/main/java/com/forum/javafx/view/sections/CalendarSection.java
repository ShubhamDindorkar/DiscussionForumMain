package com.forum.javafx.view.sections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.forum.javafx.model.CalendarEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarSection extends BorderPane {
    
    private DatePicker datePicker;
    private ListView<String> eventsList;
    private List<CalendarEvent> events;
    
    public CalendarSection() {
        this.events = new ArrayList<>();
        initialize();
        setupLayout();
    }
    
    private void initialize() {
        getStyleClass().add("calendar-section");
    }
    
    private void setupLayout() {
        // Left: Calendar
        VBox calendarBox = new VBox(15);
        calendarBox.getStyleClass().add("calendar-box");
        calendarBox.setPadding(new Insets(20));
        calendarBox.setPrefWidth(400);
        
        Label calendarLabel = new Label("📅 Calendar");
        calendarLabel.getStyleClass().add("section-header");
        
        datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("calendar-picker");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        
        Button addTaskBtn = new Button("+ Add Task");
        addTaskBtn.getStyleClass().addAll("primary-button");
        addTaskBtn.setMaxWidth(Double.MAX_VALUE);
        addTaskBtn.setOnAction(e -> showAddTaskDialog("TASK"));
        
        Button addExamBtn = new Button("+ Add Exam");
        addExamBtn.getStyleClass().addAll("primary-button");
        addExamBtn.setMaxWidth(Double.MAX_VALUE);
        addExamBtn.setOnAction(e -> showAddTaskDialog("EXAM"));
        
        calendarBox.getChildren().addAll(calendarLabel, datePicker, addTaskBtn, addExamBtn);
        
        // Right: Events list
        VBox eventsBox = new VBox(15);
        eventsBox.setPadding(new Insets(20));
        
        Label eventsLabel = new Label("Upcoming Events");
        eventsLabel.getStyleClass().add("section-header");
        
        eventsList = new ListView<>();
        eventsList.getStyleClass().add("events-list");
        VBox.setVgrow(eventsList, Priority.ALWAYS);
        
        eventsBox.getChildren().addAll(eventsLabel, eventsList);
        
        setLeft(calendarBox);
        setCenter(eventsBox);
        
        // Add sample events
        addSampleEvents();
    }
    
    private void showAddTaskDialog(String type) {
        Dialog<CalendarEvent> dialog = new Dialog<>();
        dialog.setTitle("Add " + type);
        dialog.setHeaderText("Create a new " + type.toLowerCase());
        
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        
        TextArea descArea = new TextArea();
        descArea.setPromptText("Description");
        descArea.setPrefRowCount(3);
        
        DatePicker eventDate = new DatePicker(LocalDate.now());
        
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("LOW", "MEDIUM", "HIGH", "URGENT");
        priorityBox.setValue("MEDIUM");
        
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descArea, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(eventDate, 1, 2);
        grid.add(new Label("Priority:"), 0, 3);
        grid.add(priorityBox, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String eventType = type.equals("TASK") ? "📝" : "📚";
                String priorityIcon = getPriorityIcon(priorityBox.getValue());
                String eventText = String.format("%s %s %s - %s (%s)", 
                    eventType, priorityIcon, titleField.getText(), 
                    eventDate.getValue(), priorityBox.getValue());
                eventsList.getItems().add(eventText);
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private String getPriorityIcon(String priority) {
        switch (priority) {
            case "URGENT": return "🔴";
            case "HIGH": return "🟠";
            case "MEDIUM": return "🟡";
            default: return "🟢";
        }
    }
    
    private void addSampleEvents() {
        eventsList.getItems().addAll(
            "📚 🔴 Final Exam - Data Structures (Oct 30, URGENT)",
            "📝 🟠 Project Submission - Web Dev (Oct 28, HIGH)",
            "📝 🟡 Assignment - Database (Oct 25, MEDIUM)",
            "📚 🟢 Quiz - Networks (Nov 2, LOW)"
        );
    }
}
