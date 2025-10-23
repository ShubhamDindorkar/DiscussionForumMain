package com.forum.javafx.service;

import com.forum.javafx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class SearchService {
    
    private static SearchService instance;
    
    // Data sources - will be populated by sections
    private List<Club> clubs = new ArrayList<>();
    private List<Topic> topics = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<LibraryResource> libraryResources = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    
    private SearchService() {}
    
    public static SearchService getInstance() {
        if (instance == null) {
            instance = new SearchService();
        }
        return instance;
    }
    
    /**
     * Search across all sections
     */
    public ObservableList<SearchResult> search(String query) {
        List<SearchResult> results = new ArrayList<>();
        
        if (query == null || query.trim().isEmpty()) {
            return FXCollections.observableArrayList(results);
        }
        
        String lowerQuery = query.toLowerCase().trim();
        
        // Search in clubs
        for (Club club : clubs) {
            if (club.getName().toLowerCase().contains(lowerQuery) ||
                club.getDescription().toLowerCase().contains(lowerQuery)) {
                results.add(new SearchResult(
                    club.getName(),
                    club.getDescription(),
                    "Topics",
                    "Club",
                    club
                ));
            }
        }
        
        // Search in topics
        for (Topic topic : topics) {
            if (topic.getTitle().toLowerCase().contains(lowerQuery) ||
                (topic.getAuthor() != null && topic.getAuthor().toLowerCase().contains(lowerQuery))) {
                results.add(new SearchResult(
                    topic.getTitle(),
                    "by " + topic.getAuthor(),
                    "Topics",
                    "Discussion",
                    topic
                ));
            }
        }
        
        // Search in messages
        for (Message message : messages) {
            if (message.getContent().toLowerCase().contains(lowerQuery)) {
                results.add(new SearchResult(
                    "Message from " + message.getAuthor(),
                    truncate(message.getContent(), 60),
                    "Messages",
                    "Reply",
                    message
                ));
            }
        }
        
        // Search in library resources
        for (LibraryResource resource : libraryResources) {
            if (resource.getTitle().toLowerCase().contains(lowerQuery) ||
                resource.getDescription().toLowerCase().contains(lowerQuery) ||
                resource.getAuthor().toLowerCase().contains(lowerQuery)) {
                results.add(new SearchResult(
                    resource.getTitle(),
                    resource.getDescription(),
                    "Library",
                    resource.getCategory(),
                    resource
                ));
            }
        }
        
        // Search in projects
        for (Project project : projects) {
            if (project.getName().toLowerCase().contains(lowerQuery) ||
                project.getDescription().toLowerCase().contains(lowerQuery)) {
                results.add(new SearchResult(
                    project.getName(),
                    project.getDescription(),
                    "Projects",
                    project.getStatus(),
                    project
                ));
            }
        }
        
        return FXCollections.observableArrayList(results);
    }
    
    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }
    
    // Setters for data sources
    public void setClubs(List<Club> clubs) {
        this.clubs = clubs != null ? clubs : new ArrayList<>();
    }
    
    public void setTopics(List<Topic> topics) {
        this.topics = topics != null ? topics : new ArrayList<>();
    }
    
    public void setMessages(List<Message> messages) {
        this.messages = messages != null ? messages : new ArrayList<>();
    }
    
    public void setLibraryResources(List<LibraryResource> resources) {
        this.libraryResources = resources != null ? resources : new ArrayList<>();
    }
    
    public void setProjects(List<Project> projects) {
        this.projects = projects != null ? projects : new ArrayList<>();
    }
    
    // Add methods for dynamic updates
    public void addClub(Club club) {
        if (club != null) clubs.add(club);
    }
    
    public void addTopic(Topic topic) {
        if (topic != null) topics.add(topic);
    }
    
    public void addMessage(Message message) {
        if (message != null) messages.add(message);
    }
    
    public void addLibraryResource(LibraryResource resource) {
        if (resource != null) libraryResources.add(resource);
    }
    
    public void addProject(Project project) {
        if (project != null) projects.add(project);
    }
}
