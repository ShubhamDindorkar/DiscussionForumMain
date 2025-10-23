package com.forum.javafx.model;

public class SearchResult {
    private String title;
    private String description;
    private String section; // Topics, Messages, Library, Projects
    private String category;
    private Object data; // The actual object (Topic, Message, Library item, etc.)
    
    public SearchResult(String title, String description, String section, String category, Object data) {
        this.title = title;
        this.description = description;
        this.section = section;
        this.category = category;
        this.data = data;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSection() {
        return section;
    }
    
    public void setSection(String section) {
        this.section = section;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return title + " (" + section + ")";
    }
}
