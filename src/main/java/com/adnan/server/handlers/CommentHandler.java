package com.adnan.server.handlers;

import com.adnan.server.controllers.PostController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class CommentHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PostController postController = null;
        try {
            postController = new PostController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                if (splittedPath.length == 2)  {
                    try {
                        response = postController.getComments();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 3) {
                    try {
                        response = postController.getComments(splittedPath[2]);
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
                String newComment = body.toString();
                JSONObject jsonObject = new JSONObject(newComment);
                if (splittedPath.length == 4) {
                    try {
                        response = postController.addComment(splittedPath[2], jsonObject.getString("content"), splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 5) {
                    try {
                        response = postController.updateComment(splittedPath[4], splittedPath[2], jsonObject.getString("content"), splittedPath[3]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                    response = "NOT A VALID REQUEST!!!";
                break;
            case "DELETE":
                if (splittedPath.length == 3) {
                    try {
                        response = postController.deleteComment(splittedPath[2]);
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
