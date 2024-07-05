package com.adnan.server.controllers;

import com.adnan.server.dataAccess.*;
import com.adnan.server.models.Content;
import com.adnan.server.models.Hashtag;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class HashtagController {
    private static PostDataAccess PDA = null;
    private static CommentDataAccess COMDA = null;
    private static HashtagDataAccess HDA = null;
    private static LoggedInUserDataAccess LIUDA = null;

    public HashtagController() throws SQLException {
        PDA = new PostDataAccess();
        COMDA = new CommentDataAccess();
        HDA = new HashtagDataAccess();
        LIUDA = new LoggedInUserDataAccess();
    }

    public String addHashtag(String id, String contentId) throws SQLException {
        if (!PDA.postExists(contentId) && !COMDA.commentExists(contentId))
            return "POST OR COMMENT NOT FOUND!!!";
        Content content;
        if (PDA.postExists(contentId)) {
            content = PDA.getPost(contentId);
            if (!LIUDA.getUser().equals(content.getPosterId()))
                return "NOT ALLOWED!!!";
            if (HDA.tagExists(id, contentId))
                return "ALREADY EXISTS IN THIS CONTENT!!";
            Hashtag hashtag = new Hashtag(id, contentId);
            HDA.addHashtag(hashtag);
        }
        else {
            content = COMDA.getComment(contentId);
            if (!LIUDA.getUser().equals(content.getPosterId()))
                return "NOT ALLOWED!!!";
            if (HDA.tagExists(id, contentId))
                return "ALREADY EXISTS IN THIS CONTENT!!";
            Hashtag hashtag = new Hashtag(id, contentId);
            HDA.addHashtag(hashtag);
        }
        return "successful";
    }
    public String deleteHashtag(String id, String contentId) throws SQLException {
        if (!PDA.postExists(contentId) && !COMDA.commentExists(contentId))
            return "POST OR COMMENT NOT FOUND!!!";
        Content content;
        if (PDA.postExists(contentId)) {
            content = PDA.getPost(contentId);
            if (!LIUDA.getUser().equals(content.getPosterId()))
                return "NOT ALLOWED!!!";
            Hashtag hashtag = new Hashtag(id, contentId);
            HDA.deleteHashtag(hashtag);
        }
        else {
            content = COMDA.getComment(contentId);
            if (!LIUDA.getUser().equals(content.getPosterId()))
                return "NOT ALLOWED!!!";
            Hashtag hashtag = new Hashtag(id, contentId);
            HDA.deleteHashtag(hashtag);
        }
        return "successful";
    }
    public String getContents(String id) throws SQLException, JsonProcessingException {
        ArrayList<String> contents = HDA.getHashtag(id);
        if (contents == null) return "NO POST FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(contents);
    }
}
