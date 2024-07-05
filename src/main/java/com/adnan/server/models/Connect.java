package com.adnan.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

public class Connect {
    @JsonProperty("requestSender")
    private String requestSender;
    @JsonProperty("requestReceiver")
    private String requestReceiver;

    public Connect(String requestSender, String requestReceiver) {
        this.requestSender = requestSender;
        this.requestReceiver = requestReceiver;
    }
    public Connect() {}

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

}
