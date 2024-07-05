package org.example.linkedinclient;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


public class Post extends Content {
    public Post(String posterId, String content) {
        super(posterId, content);
    }
    public Post() {
        super();
    }
}
