package com.adnan.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hashtag {
    @JsonProperty("id")
    private String id;
    @JsonProperty("contentId")
    private String contentId;

    public Hashtag(String id, String contentId) {
        this.id = id;
        this.contentId = contentId;
    }
    public Hashtag() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
