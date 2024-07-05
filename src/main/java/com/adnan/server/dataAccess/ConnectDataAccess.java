package com.adnan.server.dataAccess;

import com.adnan.server.models.Connect;
import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectDataAccess {
    private final Connection connection;
    public ConnectDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }

    public void addConnect(Connect connect) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO connects (requestSender, requestReceiver) VALUES (?,?)");
        statement.setString(1, connect.getRequestSender());
        statement.setString(2, connect.getRequestReceiver());
        statement.executeUpdate();
    }
    public void deleteConnect(Connect connect) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM connects WHERE requestSender = ? AND requestReceiver = ? AND isAccepted = 1");
        statement.setString(1, connect.getRequestSender());
        statement.setString(2, connect.getRequestReceiver());
        statement.executeUpdate();
    }

    public ArrayList<Connect> getConnects() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects");
        ArrayList<Connect> connects = new ArrayList<>();
        ResultSet resultset = statement.executeQuery();
        while (resultset.next()) {
            Connect connect = new Connect();
            connect.setRequestReceiver(resultset.getString("requestReceiver"));
            connect.setRequestSender(resultset.getString("requestSender"));
            connects.add(connect);
        }
        return connects;
    }
    public ArrayList<Connect> getConnected(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects WHERE (requestSender = ? OR requestReceiver = ?");
        statement.setString(1, userId);
        statement.setString(2, userId);
        ArrayList<Connect> connects = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            Connect connect = new Connect();
            connect.setRequestSender(resultSet.getString("requestSender"));
            connect.setRequestReceiver(resultSet.getString("requestReceiver"));
            connects.add(connect);
        }
        return connects;
    }
    public Connect getConnected(String requestSender, String requestReceiver) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects WHERE (requestSender = ? AND requestReceiver = ?) OR (requestSender = ? AND requestReceiver = ?)");
        statement.setString(1, requestSender);
        statement.setString(2, requestReceiver);
        statement.setString(3, requestReceiver);
        statement.setString(4, requestSender);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            Connect connect = new Connect();
            connect.setRequestSender(resultSet.getString("requestSender"));
            connect.setRequestReceiver(resultSet.getString("requestReceiver"));
            return connect;
        }
        return null;
    }


    public boolean isConnected(String requestSender, String requestReceiver) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects WHERE (requestSender = ? AND requestReceiver = ?) OR (requestSender = ? AND requestReceiver = ?)");
        statement.setString(1, requestSender);
        statement.setString(2, requestReceiver);
        statement.setString(3, requestReceiver);
        statement.setString(4, requestSender);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

}
