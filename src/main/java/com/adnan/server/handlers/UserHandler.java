package com.adnan.server.handlers;

import com.adnan.server.controllers.UserController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.Date;
import java.sql.SQLException;

public class UserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = null;
        try {
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
                        response = userController.getUsers();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                 else {
                    String userId = splittedPath[splittedPath.length - 1];
                    try {
                        response = userController.getUser(userId);
                        if (response == null) response = "No User";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
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
                String newUser = body.toString();
                JSONObject jsonObject = new JSONObject(newUser);
                try {
                   response = userController.createUser(jsonObject.getString("id"), jsonObject.getString("firstName"), jsonObject.getString("lastName"), jsonObject.getString("additionalName"), jsonObject.getString("country"), jsonObject.getString("city"), jsonObject.getString("email"), jsonObject.getString("password"), jsonObject.getString("phoneNumber"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "DELETE":
                if (splittedPath.length > 2) {
                    String userId = splittedPath[splittedPath.length - 1];
                    try {
                        response = userController.deleteUser(userId);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }


        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
