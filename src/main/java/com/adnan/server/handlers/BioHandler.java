package com.adnan.server.handlers;

import com.adnan.server.controllers.UserController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class BioHandler implements HttpHandler {
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
                        response = userController.getBios();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    String userId = splittedPath[splittedPath.length - 1];
                    try {
                        if (userController.getBio(userId) == null)
                            response = "no bio";
                        else
                            response = userController.getBio(userId);
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
                String newBio = body.toString();
                JSONObject jsonObject = new JSONObject(newBio);
                try {
                    response = userController.createBio(jsonObject.getString("userId"), jsonObject.getString("bioText"), jsonObject.getString("location"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            case "DELETE":
                if (splittedPath.length > 2) {
                    String userId = splittedPath[splittedPath.length - 1];
                    try {
                        response = userController.deleteBio(userId);
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
