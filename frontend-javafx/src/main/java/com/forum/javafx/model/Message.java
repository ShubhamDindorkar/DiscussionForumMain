package com.forum.javafx.model;

import java.util.Date;

public class Message {
    private long id;
    private String content;
    private Date createTime;
    private long topicId;

    public Message() {
    }

    public Message(long id, String content, Date createTime, long topicId) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.topicId = topicId;
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
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }
}
