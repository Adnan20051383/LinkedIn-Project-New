package com.adnan.server.dataAccess;

import com.adnan.server.models.Comment;
import com.adnan.server.models.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentDataAccess {
    private final Connection connection;

    public CommentDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }

    public void addComment(Comment comment) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO comments (postId, posterId, content, likesNumber, commentsNumber, timeStamp, parentId) VALUES (?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, comment.getPostId());
        statement.setString(2, comment.getPosterId());
        statement.setString(3, comment.getContent());
        statement.setInt(4, comment.getLikesNumber());
        statement.setInt(5, comment.getCommentsNumber());
        statement.setDate(6, new java.sql.Date(comment.getTimeStamp().getTime()));
        statement.setString(7, comment.getParentId());
        statement.executeUpdate();
    }
    public void updateComment(Comment comment) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE comments SET content = ?, likesNumber = ?, commentsNumber = ? WHERE postId = ?");
        statement.setString(1,comment.getContent());
        statement.setInt(2, comment.getLikesNumber());
        statement.setInt(3, comment.getCommentsNumber());
        statement.setString(4,comment.getPostId());
        statement.executeUpdate();
    }
    public void deleteComment(Comment comment) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM comments WHERE postId = ?");
        statement.setString(1, comment.getPostId());
        statement.executeUpdate();
    }
    public ArrayList<Comment> getComments() throws SQLException {
        ArrayList<Comment> posts = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM comments");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Comment post = new Comment();
            post.setPostId(resultSet.getString("postId"));
            post.setPosterId(resultSet.getString("posterId"));
            post.setContent(resultSet.getString("content"));
            post.setLikesNumber(resultSet.getInt("likesNumber"));
            post.setCommentsNumber(resultSet.getInt("commentsNumber"));
            post.setTimeStamp(resultSet.getDate("timeStamp"));
            post.setParentId(resultSet.getString("parentId"));
            posts.add(post);
        }
        return posts;
    }
    public Comment getComment(String postId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM comments WHERE postId = ?");
        statement.setString(1, postId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setPosterId(resultSet.getString("posterId"));
            comment.setContent(resultSet.getString("content"));
            comment.setCommentsNumber(resultSet.getInt("commentsNumber"));
            comment.setLikesNumber(resultSet.getInt("likesNumber"));
            comment.setTimeStamp(resultSet.getDate("timeStamp"));
            comment.setParentId(resultSet.getString("parentId"));
            return comment;
        }
        return null;
    }
    public ArrayList<Comment> getComments(String parentId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM comments where parentId = ?");
        statement.setString(1, parentId);
        ArrayList<Comment> posts = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Comment post = new Comment();
            post.setPostId(resultSet.getString("postId"));
            post.setPosterId(resultSet.getString("posterId"));
            post.setContent(resultSet.getString("content"));
            post.setLikesNumber(resultSet.getInt("likesNumber"));
            post.setCommentsNumber(resultSet.getInt("commentsNumber"));
            post.setTimeStamp(resultSet.getDate("timeStamp"));
            post.setParentId(resultSet.getString("parentId"));
            posts.add(post);
        }
        return posts;
    }
    public boolean commentExists(String postId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM comments where postId = ?");
        statement.setString(1, postId);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

}
