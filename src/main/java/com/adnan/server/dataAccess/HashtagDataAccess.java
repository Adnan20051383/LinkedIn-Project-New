package com.adnan.server.dataAccess;

import com.adnan.server.models.Hashtag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HashtagDataAccess {
    private final Connection connection;
    public HashtagDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }

    public void addHashtag(Hashtag hashtag) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO tags (id, contentId) VALUES (?,?)");
        statement.setString(1, hashtag.getId());
        statement.setString(2, hashtag.getContentId());
        statement.executeUpdate();
    }
    public void deleteHashtag(Hashtag hashtag) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM tags WHERE id = ? AND contentId = ?");
        statement.setString(1, hashtag.getId());
        statement.setString(2, hashtag.getContentId());
        statement.executeUpdate();
    }
    public ArrayList<String> getHashtag(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags WHERE id = ?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> contents = new ArrayList<>();
        while (resultSet.next()) {
            contents.add(resultSet.getString("contentId"));
        }
        return contents;
    }

    public ArrayList<String> getTags(String contentId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM tags WHERE contentId = ?");
        statement.setString(1, contentId);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> tags = new ArrayList<>();
        while (resultSet.next()) {
            tags.add(resultSet.getString("id"));
        }
        return tags;
    }
    public boolean tagExists(String id, String contentId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags WHERE id = ? AND contentId = ?");
        statement.setString(1, id);
        statement.setString(2, contentId);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}
