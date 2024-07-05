package com.adnan.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Message {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("receiver")
    private String receiver;

    @JsonProperty("text")
    private String text;

    @JsonProperty("timeStamp")
    private Date timeStamp;

    public Message () {}
    public Message(String sender, String receiver, String text) {
        this.id = sender + System.currentTimeMillis();
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.timeStamp = new Date(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
