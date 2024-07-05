package com.adnan.server.dataAccess;

import com.adnan.server.models.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikeDataAccess {
    private final Connection connection;

    public LikeDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }
    public void addLike(Like like) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO likes (liker, liked) VALUES (?, ?)");
        preparedStatement.setString(1, like.getLiker());
        preparedStatement.setString(2, like.getLiked());
        preparedStatement.executeUpdate();
    }
    public void deleteLike(Like like) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM likes WHERE liker = ? AND liked = ?");
        preparedStatement.setString(1, like.getLiker());
        preparedStatement.setString(2, like.getLiked());
        preparedStatement.executeUpdate();
    }
    public ArrayList<Like> getLikes(String userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM likes WHERE liker = ?");
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Like> likes = new ArrayList<>();
        while (resultSet.next()) {
            Like like = new Like();
            like.setLiker(resultSet.getString("liker"));
            like.setLiked(resultSet.getString("liked"));
            likes.add(like);
        }
        return likes;
    }

    public ArrayList<Like> getLikers(String postId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM likes WHERE liked = ?");
        preparedStatement.setString(1, postId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Like> likes = new ArrayList<>();
        while (resultSet.next()) {
            Like like = new Like();
            like.setLiker(resultSet.getString("liker"));
            like.setLiked(resultSet.getString("liked"));
            likes.add(like);
        }
        return likes;
    }
    public ArrayList<Like> getLikes() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM likes");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Like> likes = new ArrayList<>();
        while (resultSet.next()) {
            Like like = new Like();
            like.setLiker(resultSet.getString("liker"));
            like.setLiked(resultSet.getString("liked"));
            likes.add(like);
        }
        return likes;
    }
    public boolean isLiked(String userId, String postId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM likes WHERE liker = ? AND liked = ?");
        preparedStatement.setString(1, userId);
        preparedStatement.setString(2, postId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
