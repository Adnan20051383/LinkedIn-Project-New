package com.adnan.server.handlers;

import com.adnan.server.controllers.ConnectController;
import com.adnan.server.controllers.FollowController;
import com.adnan.server.controllers.UserController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class ConnectHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = null;
        ConnectController connectController = null;
        try {
            connectController = new ConnectController();
            userController = new UserController();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                if (splittedPath.length == 2) {
                    try {
                        response = connectController.getConnects();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 4 && splittedPath[2].equals("RequestsSent")) {
                    try {
                        response = connectController.getConnectsRequestsSent(splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 4 && splittedPath[2].equals("RequestsReceived")) {
                    try {
                        response = connectController.getConnectsRequests(splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 4 && splittedPath[2].equals("ConnectedPeople")) {
                    try {
                        response = connectController.getConnected(splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else response = "NOT A VALID REQUEST!!!";
                break;
            case "POST":
                InputStream requestBody = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                requestBody.close();
                String newConnect = body.toString();
                JSONObject jsonObject = new JSONObject(newConnect);
                if (splittedPath.length == 5 && splittedPath[2].equals("addRequest")) {
                    String id1 = splittedPath[3];
                    String id2 = splittedPath[4];
                    try {
                        response = connectController.addConnectRequest(id1, id2, jsonObject.getString("requestNote"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 5 && splittedPath[2].equals("AnswerRequest")) {
                    try {
                        response = connectController.answerRequest(splittedPath[3], splittedPath[4], jsonObject.getBoolean("isAccepted"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                    response = "NOT A VALID REQUEST!!!";
                break;
            case "DELETE":
                if (splittedPath.length == 4) {
                    try {
                        response = connectController.deleteConnect(splittedPath[2], splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                    response = "NOT A VALID REQUEST!!!";
                break;

        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
