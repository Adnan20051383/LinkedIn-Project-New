package com.adnan.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

public class Content {
    @JsonProperty("postId")
    private String postId;
    @JsonProperty("posterId")
    private String posterId;
    @JsonProperty("content")
    private String content;
    @JsonProperty("likesNumber")
    private int likesNumber;
    @JsonProperty("timeStamp")
    private Date timeStamp;
    @JsonProperty("commentsNumber")
    private int commentsNumber;
    @JsonProperty("mediaPaths")
    private ArrayList<String> mediaPaths;

    public Content(String posterId, String content) {
        this.postId = posterId + System.currentTimeMillis();
        this.posterId = posterId;
        this.content = content;
        this.timeStamp = new Date(System.currentTimeMillis());
        this.likesNumber = 0;
        this.commentsNumber = 0;
        mediaPaths = new ArrayList<>();
    }
    public Content() {
        this.likesNumber = 0;
        this.commentsNumber = 0;
        this.timeStamp = new Date(System.currentTimeMillis());
        mediaPaths = new ArrayList<>();
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikesNumber() {
        return likesNumber;
    }

    public Date getTimeStamp() {
        return  timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void addLike() {
        likesNumber++;
    }
    public void takeLike() {
        likesNumber--;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(int commentsNumber) {
        this.commentsNumber = commentsNumber;
    }
    public void addCommentsNum() {
        commentsNumber++;
    }
    public void decreaseComment() {
        commentsNumber--;
    }
    public void setLikesNumber(int likesNumber) {
        this.likesNumber = likesNumber;
    }

    public ArrayList<String> getMediaPaths() {
        return mediaPaths;
    }

    public void setMediaPaths(ArrayList<String> mediaPaths) {
        this.mediaPaths = mediaPaths;
    }
}
