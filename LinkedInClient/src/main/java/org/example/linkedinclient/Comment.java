package org.example.linkedinclient;

public class Comment extends Content {
    private String parentId;

    public Comment(String posterId, String content, String parentId) {
        super(posterId, content);
        this.parentId = parentId;
    }

    public Comment() {}

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}