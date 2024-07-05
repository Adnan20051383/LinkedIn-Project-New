package com.adnan.server.handlers;


import com.adnan.server.controllers.UserController;
import com.adnan.server.dataAccess.LoggedInUserDataAccess;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.adnan.server.utils.JwtAuth;

public class SessionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
         LoggedInUserDataAccess LIUDA = null;

        UserController userController = null;
        try {
            userController = new UserController();
            LIUDA = new LoggedInUserDataAccess();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                String claimedUserId = splittedPath[splittedPath.length - 2];
                String userPass = splittedPath[splittedPath.length - 1];
                String result = null;
                try {
                    result = userController.getUser(claimedUserId, userPass);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    response = "userID or PassWord is incorrect";
                } else {
                    Headers headers = exchange.getResponseHeaders();
                    headers.add("JWT", claimedUserId + "!" + JwtAuth.jws(claimedUserId));
                    try {
                        LIUDA.addUser(claimedUserId);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    response = "welcome " + claimedUserId + " !!!";
                }
                break;
            default:
                break;
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}