package com.adnan.server.controllers;

import com.adnan.server.dataAccess.LoggedInUserDataAccess;
import com.adnan.server.dataAccess.MessageDataAccess;
import com.adnan.server.dataAccess.UserDataAccess;
import com.adnan.server.models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class MessageController {
    private static MessageDataAccess MDA = null;
    private  static UserDataAccess UDA = null;
    private static LoggedInUserDataAccess LIUDA = null;
    public MessageController() throws SQLException {
        MDA = new MessageDataAccess();
        UDA = new UserDataAccess();
        LIUDA = new LoggedInUserDataAccess();
    }

    public String addMessage(String sender, String receiver, String text) throws SQLException {
        if (!UDA.userExists(sender) || !UDA.userExists(receiver))
            return "USER NOT FOUND!!!";
        if (!LIUDA.getUser().equals(sender))
            return "NOT ALLOWED!!!";
        Message message = new Message(sender, receiver, text);
        MDA.addMessage(message);
        return "successful";
    }
    public String deleteMessage(String id) throws SQLException {
        if (!MDA.messageExists(id))
            return "MSG NOT FOUND!!!";
        if (!LIUDA.getUser().equals(MDA.getMessage(id).getSender()))
            return "NOT ALLOWED!!!";
        MDA.deleteMessage(id);
        return "successful";
    }
    public String getMessages(String user1, String user2) throws SQLException, JsonProcessingException {
        if (!UDA.userExists(user1) || !UDA.userExists(user2))
            return "USER NOT FOUND!!!";
        if (!LIUDA.getUser().equals(user1) && !LIUDA.getUser().equals(user2))
            return "NOT ALLOWED!!!";
        ArrayList<Message> messages = MDA.getMessages(user1, user2);
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(messages);
        return response;
    }
    public String getMessagesReceived(String receiver, int cnt) throws SQLException, JsonProcessingException {
        if (!UDA.userExists(receiver))
            return "USER NOT FOUND!!!";
        if (!LIUDA.getUser().equals(receiver))
            return "NOT ALLOWED!!!";
        ArrayList<Message> messages = MDA.getMessagesReceived(receiver);
        cnt = Integer.min(cnt, messages.size());
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(messages.subList(messages.size() - cnt, messages.size()));
        return response;
    }
}
