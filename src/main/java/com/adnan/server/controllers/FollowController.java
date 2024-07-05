package com.adnan.server.controllers;

import com.adnan.server.dataAccess.FollowDataAccess;
import com.adnan.server.dataAccess.LoggedInUserDataAccess;
import com.adnan.server.dataAccess.UserDataAccess;
import com.adnan.server.models.Follow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class FollowController {
    private static UserDataAccess UDA = null;
    private static FollowDataAccess FDA = null;
    private  static LoggedInUserDataAccess LIUDA = null;
    public FollowController() throws SQLException {
        UDA = new UserDataAccess();
        FDA = new FollowDataAccess();
        LIUDA = new LoggedInUserDataAccess();
    }
    public String createFollow(String follower, String followed) throws SQLException {
        if (!UDA.userExists(followed) || !UDA.userExists((follower)))
            return "USER NOT FOUND!!!";
        if (!follower.equals(LIUDA.getUser()))
            return "NOT ALLOWED!!!";
        if (!FDA.isFollowing(follower, followed)) {
            Follow follow = new Follow(follower, followed);
            FDA.addFollow(follow);
            UDA.updateFollower(followed);
            UDA.updateFollowing(follower);
            return "successful!";
        }
        else return "ALREADY FOLLOWED!!!";
    }

    public String deleteFollow(String follower, String followed) throws SQLException {
        if (!UDA.userExists(follower) || !UDA.userExists(followed))
            return "USER NOT FOUND!!!";
        if (!follower.equals(LIUDA.getUser()))
            return "NOT ALLOWED!!!";
        if (FDA.isFollowing(follower, followed)) {
            Follow follow = new Follow(follower, followed);
            FDA.deleteFollow(follow);
            UDA.updateFollower(followed);
            UDA.updateFollowing(follower);
            return "successful!";
        }
        else
            return "DID NOT FOLLOWED TO DELETE!!!";
    }


    public String getFollows(String userId) throws SQLException, JsonProcessingException {
        if (!UDA.userExists(userId))
            return "USER NOT FOUND!!!";
        ArrayList<Follow> follows = FDA.getFollows(userId);
        if (follows == null) return "NO FOLLOWINGS FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }

    public String getFollowers(String userId) throws SQLException, JsonProcessingException {
        if (!UDA.userExists(userId))
            return "USER NOT FOUND!!!";
        ArrayList<Follow> follows = FDA.getFollowers(userId);
        if (follows == null) return "NO FOLLOWERS FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }

    public String getFollows() throws SQLException, JsonProcessingException {
        ArrayList<Follow> follows = FDA.getFollows();
        if (follows == null) return "NO FOLLOWS FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }
}
