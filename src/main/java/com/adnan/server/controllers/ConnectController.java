package com.adnan.server.controllers;

import com.adnan.server.dataAccess.*;
import com.adnan.server.models.Connect;
import com.adnan.server.models.ConnectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectController {
    private static UserDataAccess UDA = null;
    private static ConnectDataAccess CDA = null;

    private static ConnectRequestDataAccess CRDA = null;
    private  static LoggedInUserDataAccess LIUDA = null;

    public ConnectController() throws SQLException {
        UDA = new UserDataAccess();
        CDA = new ConnectDataAccess();
        CRDA = new ConnectRequestDataAccess();
        LIUDA = new LoggedInUserDataAccess();
    }

    public String addConnectRequest(String requestSender, String requestReceiver, String requestNote) throws SQLException {
        if (!UDA.userExists(requestSender) || !UDA.userExists(requestReceiver))
            return "USER NOT FOUND!!!";
        if (!requestSender.equals(LIUDA.getUser()))
            return "NOT ALLOWED!!!";
        if (CRDA.isRequestSent(requestSender, requestReceiver))
            return "ALREADY SENT!!!";
        if (CDA.isConnected(requestSender, requestReceiver))
            return "ALREADY CONNECTED!!!";
        ConnectRequest connectRequest = new ConnectRequest(requestSender, requestReceiver, requestNote);
        CRDA.addRequest(connectRequest);
        return "successful";
    }

    public String answerRequest(String requestSender, String requestReceiver, boolean isAccepted) throws SQLException {
        if (!UDA.userExists(requestSender) || !UDA.userExists(requestReceiver))
            return "USER NOT FOUND!!!";
        if (!requestReceiver.equals(LIUDA.getUser()))
            return "NOT ALLOWED!!!";
        ConnectRequest connectRequest = CRDA.getRequest(requestSender, requestReceiver);
        CRDA.deleteRequest(connectRequest);
        if (isAccepted)
            return addConnect(requestSender, requestReceiver);
        else
            return "GOT REJECTED !!!";
    }
    private String addConnect(String requestSender, String requestReceiver) throws SQLException {
        Connect connect = new Connect(requestSender, requestReceiver);
        CDA.addConnect(connect);
        return "successful";
    }
    public String getConnectRequest(String requestSender, String requestReceiver) throws SQLException, JsonProcessingException {
        if (!UDA.userExists(requestSender) || !UDA.userExists(requestReceiver))
            return "USER NOT FOUND!!!";
        if (!requestReceiver.equals(LIUDA.getUser()))
            return "NOT ALLOWED!!!";
        ConnectRequest connectRequest = CRDA.getRequest(requestSender, requestReceiver);
        if (connectRequest == null) return "NO REQUEST FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(connectRequest);
    }
    public String getConnectsRequests(String requestReceiver) throws SQLException, JsonProcessingException {
        if (!UDA.userExists(requestReceiver))
            return "USER NOT FOUND!!!";
        if (!requestReceiver.equals(LIUDA.getUser()))
            return "NOT ALLOWED!!!";
        ArrayList<ConnectRequest> connects = CRDA.getConnectRequests(requestReceiver);
        if (connects == null) return "NO REQUEST FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(connects);
    }
    public String getConnectsRequestsSent(String requestSender) throws SQLException, JsonProcessingException {
        if (!UDA.userExists(requestSender))
            return "USER NOT FOUND!!!";
        if (!requestSender.equals(LIUDA.getUser()))
            return "NOT ALLOWED!!!";
        ArrayList<ConnectRequest> connects = CRDA.getConnectRequestsSent(requestSender);
        if (connects == null) return "NO REQUEST HAS BEEN SENT!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(connects);
    }
    public  String getConnects() throws SQLException, JsonProcessingException {
        ArrayList<Connect> connects = CDA.getConnects();
        if (connects == null) return "NO REQUEST FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(connects);
    }
    public String getConnected(String userId) throws SQLException, JsonProcessingException {
        if (!UDA.userExists(userId))
            return "NO USER FOUND!!!";
        if (!LIUDA.getUser().equals(userId))
            return "NOT ALLOWED";
        ArrayList<Connect> connects = CDA.getConnected(userId);
        if (connects == null) return "NO CONNECTED USER FOUND!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(connects);
    }
    public String getConnected(String requestSender, String requestReceiver) throws SQLException, JsonProcessingException {
        if (!UDA.userExists(requestReceiver) || !UDA.userExists(requestSender))
            return "NO USER FOUND!!!";
        if (!LIUDA.getUser().equals(requestSender) && !LIUDA.getUser().equals(requestReceiver))
            return "NOT ALLOWED";
        Connect connect = CDA.getConnected(requestSender, requestReceiver);
        if (connect == null) return "NO CONNECTED USER!!!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(connect);
    }
    public String deleteConnect(String requestSender, String requestReceiver) throws SQLException {
        if (!UDA.userExists(requestSender) || !UDA.userExists(requestReceiver))
            return "USER NOT FOUND!!!";
        if (!requestReceiver.equals(LIUDA.getUser()) && !requestSender.equals(LIUDA.getUser()))
            return "NOT ALLOWED!!!";
        Connect connect = CDA.getConnected(requestSender, requestReceiver);
        CDA.deleteConnect(connect);
        return "successful";
    }

}
