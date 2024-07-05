package com.adnan.server.controllers;

import com.adnan.server.dataAccess.*;
import com.adnan.server.models.Comment;
import com.adnan.server.models.Content;
import com.adnan.server.models.Like;
import com.adnan.server.models.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikeController {
    private final LikeDataAccess LDA;
    private final LoggedInUserDataAccess LIUDA;
    private final UserDataAccess UDA;
    private final PostDataAccess PDA;
    private final CommentDataAccess COMDA;
    public LikeController() throws SQLException {
        LDA = new LikeDataAccess();
        LIUDA = new LoggedInUserDataAccess();
        UDA = new UserDataAccess();
        PDA = new PostDataAccess();
        COMDA = new CommentDataAccess();
    }
    public String addLike(String liker, String liked) throws SQLException {
        System.out.println(liker + "    " + liked);
        if (!UDA.userExists(liker) || (!PDA.postExists(liked) && !COMDA.commentExists(liked)))
            return "USER OR POST  NOT FOUND!!";
        if (!LIUDA.getUser().equals(liker))
            return "NOT ALLOWED!!!";
        if (LDA.isLiked(liker, liked))
            return "ALREADY LIKED!!!";
        Like like = new Like(liker, liked);
        Content post;
        if (PDA.postExists(liked)) {
            post = PDA.getPost(liked);
            post.addLike();
            PDA.updatePost((Post) post);
        }
        if (COMDA.commentExists(liked)) {
            post = COMDA.getComment(liked);
            post.addLike();
            COMDA.updateComment((Comment) post);
        }
        LDA.addLike(like);
        return "successful";

    }
    public String deleteLike(String liker, String liked) throws SQLException {
        if (!UDA.userExists(liker) || (!PDA.postExists(liked) && !COMDA.commentExists(liked)))
            return "USER OR POST  NOT FOUND!!";
        if (!LIUDA.getUser().equals(liker))
            return "NOT ALLOWED!!!";
        if (!LDA.isLiked(liker, liked))
            return "DID NOT LIKE TO DELETE!!!";
        Like like = new Like(liker, liked);
        Content post;
        if (PDA.postExists(liked)) {
            post = PDA.getPost(liked);
            post.takeLike();
            PDA.updatePost((Post) post);
        }
        if (COMDA.commentExists(liked)) {
            post = COMDA.getComment(liked);
            post.takeLike();
            COMDA.updateComment((Comment) post);
        }
        LDA.deleteLike(like);
        return "successful";
    }


    public String getLikes(String userId) throws SQLException, JsonProcessingException {
        if (!UDA.userExists(userId))
            return "USER NOT FOUND!!!";
        ArrayList<Like> likes = LDA.getLikes(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(likes);
    }

    public String getLikers(String postId) throws SQLException, JsonProcessingException {
        if (!PDA.postExists(postId) && !COMDA.commentExists(postId))
            return "POST NOT FOUND!!!";
        ArrayList<Like> likes = LDA.getLikers(postId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(likes);
    }

    public String getLikes() throws SQLException, JsonProcessingException {
        ArrayList<Like> likes = LDA.getLikes();
        if (likes == null) return "NO LIKES FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(likes);
    }
}
