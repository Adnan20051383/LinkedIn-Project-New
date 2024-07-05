package com.adnan.server.dataAccess;

import com.adnan.server.models.Bio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BioDataAccess {
    private final Connection connection;
    public BioDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }
    public void addBio(Bio bio) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO bios (userId, bioText, location) VALUES (?, ?, ?)");
        statement.setString(1, bio.getUserId());
        statement.setString(2, bio.getBioText());
        statement.setString(3, bio.getLocation());
        statement.executeUpdate();
    }
    public void deleteBio(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM bios WHERE userId = ?");
        statement.setString(1, userId);
        statement.executeUpdate();
    }
    public void updateBio(Bio bio) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE bios SET bioText = ?, location = ? WHERE userId = ?");
        statement.setString(1, bio.getBioText());
        statement.setString(2, bio.getLocation());
        statement.setString(3, bio.getUserId());
        statement.executeUpdate();
    }
    public Bio getBio(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM bios WHERE userId = ?");
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Bio bio = new Bio();
            bio.setUserId(resultSet.getString("userId"));
            bio.setBioText(resultSet.getString("bioText"));
            bio.setLocation(resultSet.getString("location"));
            return bio;
        }

        return null;
    }
    public ArrayList<Bio> getBios() throws SQLException {
        ArrayList<Bio> bios = new ArrayList<Bio>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM bios");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Bio bio = new Bio();
            bio.setUserId(resultSet.getString("userId"));
            bio.setBioText(resultSet.getString("bioText"));
            bio.setLocation(resultSet.getString("location"));
            bios.add(bio);
        }

        return bios;
    }
    public boolean bioExists(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select exists(select *from bios where userId = ?)");
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();
        int a = 0;
        while (resultSet.next()) {
            a = resultSet.getInt(1);
        }
        return a == 1;
    }


}
