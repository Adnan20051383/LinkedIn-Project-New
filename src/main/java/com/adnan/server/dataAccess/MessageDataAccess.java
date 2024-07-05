package com.adnan.server.dataAccess;

import com.adnan.server.models.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageDataAccess {
    private final Connection connection;

    public MessageDataAccess() throws SQLException {
        connection = MainDataBase.getConnection();
    }

    public void addMessage(Message message) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages (id, sender, receiver, text, timeStamp) VALUES (?, ?, ?, ?, ?)");
        preparedStatement.setString(1, message.getId());
        preparedStatement.setString(2, message.getSender());
        preparedStatement.setString(3, message.getReceiver());
        preparedStatement.setString(4, message.getText());
        preparedStatement.setDate(5, new java.sql.Date(message.getTimeStamp().getTime()));
        preparedStatement.executeUpdate();
    }

    public void deleteMessage(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM messages WHERE id = ?");
        preparedStatement.setString(1, id);
        preparedStatement.executeUpdate();
    }

    public ArrayList<Message> getMessages(String user1, String user2) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE (sender = ? AND receiver = ?) or (sender = ? AND receiver = ?)");
        statement.setString(1, user1);
        statement.setString(2, user2);
        statement.setString(3, user2);
        statement.setString(4, user1);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Message message = new Message();
            message.setId(resultSet.getString("id"));
            message.setSender(resultSet.getString("sender"));
            message.setReceiver(resultSet.getString("receiver"));
            message.setText(resultSet.getString("text"));
            message.setTimeStamp(resultSet.getDate("timeStamp"));
            messages.add(message);
        }
        return messages;
    }
    public Message getMessage(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE id = ?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Message message = new Message();
            message.setId(id);
            message.setSender(resultSet.getString("sender"));
            message.setReceiver(resultSet.getString("receiver"));
            message.setText(resultSet.getString("text"));
            message.setTimeStamp(resultSet.getDate("timeStamp"));
            return message;
        }
        return null;
    }

    public ArrayList<Message> getMessagesReceived(String receiver) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE receiver = ?");
        statement.setString(1, receiver);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Message message = new Message();
            message.setId(resultSet.getString("id"));
            message.setSender(resultSet.getString("sender"));
            message.setReceiver(resultSet.getString("receiver"));
            message.setText(resultSet.getString("text"));
            message.setTimeStamp(resultSet.getDate("timeStamp"));
            messages.add(message);
        }
        return messages;
    }
    public boolean messageExists(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE id = ?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}
