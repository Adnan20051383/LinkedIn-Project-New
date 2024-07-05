package com.adnan.server.dataAccess;

import com.adnan.server.models.Bio;
import com.adnan.server.models.Post;

import java.sql.*;
import java.util.ArrayList;

public class PostDataAccess {
    private static Connection connection;

    public PostDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }
    public void addPost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO posts (postId, posterId, content, likesNumber, commentsNumber, timeStamp) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setString(1, post.getPostId());
        statement.setString(2, post.getPosterId());
        statement.setString(3, post.getContent());
        statement.setInt(4, post.getLikesNumber());
        statement.setInt(5, post.getCommentsNumber());
        statement.setDate(6, new java.sql.Date(post.getTimeStamp().getTime()));
        statement.executeUpdate();
    }
    public void updatePost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE posts SET content = ?, likesNumber = ?, commentsNumber = ? WHERE postId = ?");
        statement.setString(1,post.getContent());
        statement.setInt(2, post.getLikesNumber());
        statement.setInt(3, post.getCommentsNumber());
        statement.setString(4,post.getPostId());
        statement.executeUpdate();
    }
    public void deletePost(String postId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM posts WHERE postId = ?");
        statement.setString(1, postId);
        statement.executeUpdate();
    }
    public Post getPost(String postId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE postId = ?");
        statement.setString(1, postId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Post post = new Post();
            post.setPostId(resultSet.getString("postId"));
            post.setPosterId(resultSet.getString("posterId"));
            post.setContent(resultSet.getString("content"));
            post.setLikesNumber(resultSet.getInt("likesNumber"));
            post.setCommentsNumber(resultSet.getInt("commentsNumber"));
            post.setTimeStamp(resultSet.getDate("timeStamp"));
            return post;
        }
        return null;
    }
    public ArrayList<Post> getPosts() throws SQLException {
        ArrayList<Post> posts = new ArrayList<Post>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Post post = new Post();
            post.setPostId(resultSet.getString("postId"));
            post.setPosterId(resultSet.getString("posterId"));
            post.setContent(resultSet.getString("content"));
            post.setLikesNumber(resultSet.getInt("likesNumber"));
            post.setCommentsNumber(resultSet.getInt("commentsNumber"));
            post.setTimeStamp(resultSet.getDate("timeStamp"));
            posts.add(post);
        }
        return posts;
    }
    public boolean postExists(String postId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select exists(select *from posts where postId = ?)");
        statement.setString(1, postId);
        ResultSet resultSet = statement.executeQuery();
        int a = 0;
        while (resultSet.next()) {
            a = resultSet.getInt(1);
        }
        return a == 1;
    }
}
