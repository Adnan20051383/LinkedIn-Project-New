package com.adnan.server.handlers;

import com.adnan.server.controllers.FollowController;
import com.adnan.server.controllers.SkillController;
import com.adnan.server.controllers.UserController;
import com.adnan.server.dataAccess.LoggedInUserDataAccess;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class FollowHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = null;
        FollowController followController = null;
        try {
            followController = new FollowController();
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
                        response = followController.getFollows();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 4 && splittedPath[2].equals("followers")) {
                    try {
                        response = followController.getFollowers(splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 4 && splittedPath[2].equals("followings")) {
                    try {
                        response = followController.getFollows(splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else response = "NOT A VALID REQUEST!!!";
                break;
            case "POST":
                if (splittedPath.length == 4) {
                    try {
                        response = followController.createFollow(splittedPath[2], splittedPath[3]);
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
                        response = followController.deleteFollow(splittedPath[2], splittedPath[3]);
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
