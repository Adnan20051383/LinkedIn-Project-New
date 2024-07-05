package com.adnan.server.dataAccess;

import com.adnan.server.models.Follow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowDataAccess {
    private final Connection connection;
    public FollowDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }
    public void addFollow(Follow follow) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO follows (follower, followed) VALUES (?, ?)");
        statement.setString(1, follow.getFollower());
        statement.setString(2, follow.getFollowed());
        statement.executeUpdate();
    }
    public void deleteFollow(Follow follow) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM follows WHERE follower = ? AND followed = ?");
        statement.setString(1, follow.getFollower());
        statement.setString(2, follow.getFollowed());
        statement.executeUpdate();
    }
    public ArrayList<Follow> getFollows(String userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follows WHERE follower = ?");
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Follow> follows = new ArrayList<>();
        while (resultSet.next()) {
            Follow follow = new Follow();
            follow.setFollower(resultSet.getString("follower"));
            follow.setFollowed(resultSet.getString("followed"));
            follows.add(follow);
        }
        return follows;
    }
    public ArrayList<Follow> getFollowers(String userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follows WHERE followed = ?");
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Follow> follows = new ArrayList<>();
        while (resultSet.next()) {
            Follow follow = new Follow();
            follow.setFollower(resultSet.getString("follower"));
            follow.setFollowed(resultSet.getString("followed"));
            follows.add(follow);
        }
        return follows;
    }

    public ArrayList<Follow> getFollows() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follows");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Follow> follows = new ArrayList<>();
        while (resultSet.next()) {
            Follow follow = new Follow();
            follow.setFollower(resultSet.getString("follower"));
            follow.setFollowed(resultSet.getString("followed"));
            follows.add(follow);
        }
        return follows;
    }
    public boolean isFollowing(String followerId, String followedId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follows WHERE follower = ? AND followed = ?");
        preparedStatement.setString(1, followerId);
        preparedStatement.setString(2, followedId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
    public int countFollowers(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM follows WHERE followed = ?");
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();
        int a = 0;
        if (resultSet.next())
            a = resultSet.getInt(1);
        return a;
    }
    public int countFollowings(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM follows WHERE follower = ?");
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();
        int a = 0;
        if (resultSet.next())
            a = resultSet.getInt(1);
        return a;
    }
}
