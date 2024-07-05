package com.adnan.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


public class Post extends Content {
    @JsonProperty
    private boolean hasMedia;
    public Post(String posterId, String content) {
        super(posterId, content);
        hasMedia = false;
    }
    public Post() {
        super();
        hasMedia = false;
    }

    public void setHasMedia(boolean hasMedia) {
        this.hasMedia = hasMedia;
    }

    public boolean HasMedia() {
        return hasMedia;
    }
}
