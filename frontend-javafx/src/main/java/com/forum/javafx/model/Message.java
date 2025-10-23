package com.forum.javafx.model;

import java.util.Date;

public class Message {
    private long id;
    private String content;
    private Date createTime;
    private long topicId;
    private String author;
    private Date timestamp;

    public Message() {
    }

    public Message(long id, String content, Date createTime, long topicId) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.topicId = topicId;
        this.timestamp = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        this.timestamp = createTime;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public Date getTimestamp() { return timestamp != null ? timestamp : createTime; }
    public void setTimestamp(Date timestamp) { 
        this.timestamp = timestamp;
        this.createTime = timestamp;
    }
}
