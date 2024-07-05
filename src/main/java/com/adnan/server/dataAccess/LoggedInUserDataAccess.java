package com.adnan.server.dataAccess;

import com.adnan.server.models.LoggedInUser;

import java.sql.*;

public class LoggedInUserDataAccess {
    private final Connection connection;
    public LoggedInUserDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }
    public void addUser(String id) throws SQLException {
        LoggedInUser loggedInUser = new LoggedInUser(id);
        if (getLoggedNumber() == 1)
            deleteUser();
        PreparedStatement statement = connection.prepareStatement("insert into logged_in_user (userId, logInTime) VALUES (?, ?)");
        statement.setString(1, id);
        statement.setDate(2, new Date(System.currentTimeMillis()));
        statement.executeUpdate();
    }
    public int getLoggedNumber() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM logged_in_user");
        ResultSet resultSet = statement.executeQuery();
        int a = 0;
        if (resultSet.next())
            a = resultSet.getInt(1);
        return a;
    }
    public String getUser() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from logged_in_user");
        ResultSet resultSet = statement.executeQuery();
        String id = null;
        if (resultSet.next())
            id = resultSet.getString(1);
        return id;
    }
    public void deleteUser() throws SQLException {
        if (getLoggedNumber() == 1) {
            PreparedStatement statement = connection.prepareStatement("delete from logged_in_user");
            statement.executeUpdate();
        }
        else
            return;
    }

}



