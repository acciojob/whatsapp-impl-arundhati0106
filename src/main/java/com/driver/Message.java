package com.driver;

import java.util.Date;

public class Message {
    //attributes
    private int id;
    private String content;
    private Date timestamp;

    //constructor
    public Message(int id, String content) {
        this.id = id;
        this.content = content;
    }

    //getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContent() { return content;}
    public void setContent(String content) { this.content = content; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}