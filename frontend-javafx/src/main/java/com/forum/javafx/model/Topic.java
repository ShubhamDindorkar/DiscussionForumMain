package com.forum.javafx.model;

public class Topic {
    private long id;
    private String title;
    private int messageCount;

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
}
