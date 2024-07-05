package com.adnan.server.handlers;

import com.adnan.server.controllers.LikeController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class LikeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LikeController likeController = null;
        try {
            likeController = new LikeController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                if (splittedPath.length == 2) {
                    try {
                        response = likeController.getLikes();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 4 && splittedPath[2].equals("likers")) {
                    try {
                        response = likeController.getLikers(splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 4 && splittedPath[2].equals("liked")) {
                    try {
                        response = likeController.getLikes(splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else response = "NOT A VALID REQUEST!!!";
                break;
            case "POST":
                if (splittedPath.length == 4) {
                    try {
                        System.out.println(splittedPath[2] + "   " + splittedPath[3]);
                        response = likeController.addLike(splittedPath[2], splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else response = "NOT A VALID REQUEST!!!";
                break;
            case "DELETE":
                if (splittedPath.length == 4) {
                    try {
                        response = likeController.deleteLike(splittedPath[2], splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else response = "NOT A VALID REQUEST!!!";
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
