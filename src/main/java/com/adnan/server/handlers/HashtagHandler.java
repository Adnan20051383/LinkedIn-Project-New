package com.adnan.server.handlers;

import com.adnan.server.controllers.HashtagController;
import com.adnan.server.controllers.MessageController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class HashtagHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HashtagController hashtagController;
        try {
            hashtagController = new HashtagController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                if (splittedPath.length == 3) {
                    try {
                        response = hashtagController.getContents(splittedPath[2]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else response = "NOT A VALID REQUEST!!!";
                break;
            case "POST":
                if (splittedPath.length == 4) {
                    try {
                        response = hashtagController.addHashtag(splittedPath[2], splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else response = "NOT A VALID REQUEST!!!";
                break;
            case "DELETE":
                if (splittedPath.length == 4) {
                    try {
                        response = hashtagController.deleteHashtag(splittedPath[2], splittedPath[3]);
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
