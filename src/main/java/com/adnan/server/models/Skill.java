package com.adnan.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Skill {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("nthSkill")
    private int nthSkill;
    @JsonProperty("text")
    private String text;

    public Skill(String userId, int nthSkill, String text) {
        this.userId = userId;
        this.nthSkill = nthSkill;
        this.text = text;
    }

    public Skill() {}
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNthSkill() {
        return nthSkill;
    }

    public void setNthSkill(int nthSkill) {
        this.nthSkill = nthSkill;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
