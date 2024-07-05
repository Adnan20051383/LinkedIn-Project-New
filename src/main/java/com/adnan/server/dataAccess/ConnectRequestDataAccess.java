package com.adnan.server.dataAccess;

import com.adnan.server.models.Connect;
import com.adnan.server.models.ConnectRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectRequestDataAccess {
    private final Connection connection;
    public ConnectRequestDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }

    public void addRequest(ConnectRequest connectRequest) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO connectRequests (requestSender, requestReceiver, requestNote) VALUES (?,?,?)");
        statement.setString(1, connectRequest.getRequestSender());
        statement.setString(2, connectRequest.getRequestReceiver());
        statement.setString(3, connectRequest.getRequestNote());
        statement.executeUpdate();
    }
    public void updateRequest(ConnectRequest connectRequest) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE connectRequests SET  isAccepted = ? WHERE requestSender = ? AND requestReceiver = ?");
        statement.setBoolean(1, connectRequest.isAccepted());
        statement.setString(2, connectRequest.getRequestSender());
        statement.setString(3, connectRequest.getRequestReceiver());
        statement.executeUpdate();
    }
    public void deleteRequest(ConnectRequest connectRequest) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM connectRequests WHERE requestSender = ? AND requestReceiver = ?");
        statement.setString(1, connectRequest.getRequestSender());
        statement.setString(2, connectRequest.getRequestReceiver());
        statement.executeUpdate();
    }
    public ConnectRequest getRequest(String requestSender, String requestReceiver) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connectRequests WHERE requestSender = ? AND requestReceiver = ?");
        statement.setString(1, requestSender);
        statement.setString(2, requestReceiver);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            ConnectRequest connectRequest = new ConnectRequest();
            connectRequest.setRequestSender(requestSender);
            connectRequest.setRequestReceiver(requestReceiver);
            connectRequest.setRequestNote(resultSet.getString("requestNote"));
            connectRequest.setAccepted(resultSet.getBoolean("isAccepted"));
            return connectRequest;
        }
        return null;
    }
    public ArrayList<ConnectRequest> getConnectRequests(String requestReceiver) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connectRequests WHERE requestReceiver = ?");
        statement.setString(1, requestReceiver);
        ArrayList<ConnectRequest> connects = new ArrayList<>();
        ResultSet resultset = statement.executeQuery();
        while (resultset.next()) {
            ConnectRequest connect = new ConnectRequest();
            connect.setRequestSender(resultset.getString("requestSender"));
            connect.setRequestReceiver(requestReceiver);
            connects.add(connect);
        }
        return connects;
    }
    public ArrayList<ConnectRequest> getConnectRequestsSent(String requestSender) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connectRequests WHERE requestSender = ?");
        statement.setString(1, requestSender);
        ArrayList<ConnectRequest> connects = new ArrayList<>();
        ResultSet resultset = statement.executeQuery();
        while (resultset.next()) {
            ConnectRequest connect = new ConnectRequest();
            connect.setRequestReceiver(resultset.getString("requestReceiver"));
            connect.setRequestSender(requestSender);
            connects.add(connect);
        }
        return connects;
    }

    public boolean isRequestSent(String requestSender, String requestReceiver) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connectRequests WHERE requestSender = ? AND requestReceiver = ?");
        statement.setString(1, requestSender);
        statement.setString(2, requestReceiver);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}
