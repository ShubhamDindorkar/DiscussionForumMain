package com.forum.javafx.model;

import java.util.Date;

public class Topic {
    private long id;
    private String title;
    private int messageCount;
    private String author;
    private int replyCount;
    private int viewCount;
    private Date timestamp;

    public Topic() {
    }

    public Topic(long id, String title, int messageCount) {
        this.id = id;
        this.title = title;
        this.messageCount = messageCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public int getReplyCount() { return replyCount; }
    public void setReplyCount(int replyCount) { 
        this.replyCount = replyCount;
        this.messageCount = replyCount; // Sync with messageCount
    }
    
    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }
    
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
