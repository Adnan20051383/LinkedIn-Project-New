package com.adnan.server.models;

import java.util.Date;

public class LoggedInUser {
    private String userId;
    private Date loggedInTime;

    public LoggedInUser(String userId) {
        this.userId = userId;
        this.loggedInTime = new Date(System.currentTimeMillis());
    }

    public String getUserId() {
        return userId;
    }

}
