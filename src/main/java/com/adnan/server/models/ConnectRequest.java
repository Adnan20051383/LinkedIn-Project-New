package com.adnan.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectRequest {
    @JsonProperty("requestSender")
    private String requestSender;
    @JsonProperty("requestReceiver")
    private String requestReceiver;
    @JsonProperty("requestNote")
    private String requestNote;
    @JsonProperty("isAccepted")
    private Boolean isAccepted;

    public ConnectRequest(String requestSender, String requestReceiver, String requestNote) {
        this.requestSender = requestSender;
        this.requestReceiver = requestReceiver;
        this.requestNote = requestNote;
        isAccepted = null;
    }
    public ConnectRequest() {}

    public String getRequestSender() {
        return requestSender;
    }

    public void setRequestSender(String requestSender) {
        this.requestSender = requestSender;
    }

    public String getRequestReceiver() {
        return requestReceiver;
    }

    public void setRequestReceiver(String requestReceiver) {
        this.requestReceiver = requestReceiver;
    }

    public String getRequestNote() {
        return requestNote;
    }

    public void setRequestNote(String requestNote) {
        this.requestNote = requestNote;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
