package com.adnan.server.controllers;

import com.adnan.server.dataAccess.*;
import com.adnan.server.models.Comment;
import com.adnan.server.models.Content;
import com.adnan.server.models.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class PostController {
    private static PostDataAccess PDA = null;
    private static UserDataAccess UDA = null;
    private static CommentDataAccess COMDA = null;
    private static LoggedInUserDataAccess LIUDA = null;

    public PostController() throws SQLException {
        PDA = new PostDataAccess();
        COMDA = new CommentDataAccess();
        UDA = new UserDataAccess();
        LIUDA = new LoggedInUserDataAccess();
    }
    public String getPost(String postId) throws SQLException, JsonProcessingException {
        Post post = PDA.getPost(postId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(post);
    }
    public String getPosts() throws SQLException, JsonProcessingException {
        ArrayList<Post> posts = PDA.getPosts();
        if (posts == null) return "NO POST!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(posts);
    }
    public String createPost(String posterId, String content) throws SQLException {
        if(!UDA.userExists(posterId))
            return "USER NOT FOUND!!!";
        Post post = new Post(posterId, content);
        PDA.addPost(post);
        return post.getPostId();
    }
    public String updatePost(String postId, String posterId, String content) throws SQLException {
        if (!PDA.postExists(postId) || !UDA.userExists(posterId))
            return "USER OR POST NOT FOUND!!!";
        Post post = PDA.getPost(postId);
        post.setContent(content);
        PDA.updatePost(post);
        return "successful";
    }
    public String deletePost(String postId) throws SQLException {
        if(!PDA.postExists(postId))
            return "POST NOT FOUND!!!";
        PDA.deletePost(postId);
        return "successful!";
    }
    public String addComment(String posterId, String content, String parentId) throws SQLException {
        if (!UDA.userExists(posterId) || (!PDA.postExists(parentId) && !COMDA.commentExists(parentId)))
            return "SOMETHING'S WRONG!!!";
        if (!LIUDA.getUser().equals(posterId))
            return "NOT ALLOWED!!!";
        Comment comment = new Comment(posterId, content, parentId);
        Content post;
        if (PDA.postExists((parentId))) {
            post = PDA.getPost(parentId);
            post.addCommentsNum();
            PDA.updatePost((Post) post);
        }
        else {
            post = COMDA.getComment(parentId);
            post.addCommentsNum();
            COMDA.updateComment((Comment) post);
        }
        COMDA.addComment(comment);
        return comment.getPostId();
    }
    public String deleteComment(String postId) throws SQLException {
        if (!COMDA.commentExists(postId))
            return "NO COMMENTS FOUND!!!";
        Comment comment = COMDA.getComment(postId);
        if (!comment.getPosterId().equals(LIUDA.getUser()))
            return "NOT ALLOWED!!!";
        Content post;

        if (PDA.postExists((comment.getParentId()))) {
            post = PDA.getPost(comment.getParentId());
            post.decreaseComment();
            PDA.updatePost((Post) post);
        }
        else {
            post = COMDA.getComment(comment.getParentId());
            post.decreaseComment();
            COMDA.updateComment((Comment) post);
        }
        COMDA.deleteComment(comment);
        return "successful";
    }
    public String updateComment(String postId,String posterId, String content, String parentId) throws SQLException {
        if (!UDA.userExists(posterId) || (!PDA.postExists(parentId) && !COMDA.commentExists(parentId)) || !COMDA.commentExists(postId))
            return "SOMETHING'S WRONG!!!";
        if (!LIUDA.getUser().equals(posterId))
            return "NOT ALLOWED!!!";
        Comment comment = COMDA.getComment(postId);
        comment.setContent(content);
        COMDA.updateComment(comment);
        return "successful";
    }
    public String getComments() throws SQLException, JsonProcessingException {
        ArrayList<Comment> comments = COMDA.getComments();
        if (comments == null) return "NO COMMENTS!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(comments);
    }
    public String getComments(String parentId) throws SQLException, JsonProcessingException {
        if (!COMDA.commentExists(parentId) && !PDA.postExists(parentId))
            return "NO POST OR COMMENT FOUND !!!";
        ArrayList<Comment> comments = COMDA.getComments(parentId);
        if (comments == null) return "NO  COMMENTS FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(comments);
    }
    public String getComment(String postId) throws SQLException, JsonProcessingException {
        if (!COMDA.commentExists(postId))
            return "NO COMMENT FOUND!!!";
        Comment comment = COMDA.getComment(postId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(comment);
    }
}
