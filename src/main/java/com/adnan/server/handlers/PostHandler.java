package com.adnan.server.handlers;

import com.adnan.server.controllers.PostController;
import com.adnan.server.controllers.UserController;
import com.adnan.server.dataAccess.LoggedInUserDataAccess;
import com.adnan.server.utils.ExtractUserAuth;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class PostHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PostController postController = null;
        LoggedInUserDataAccess LIUDA = null;
        try {
            postController = new PostController();
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
                if (splittedPath.length == 2) {
                    try {
                        response = postController.getPosts();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    String postId = splittedPath[splittedPath.length - 1];
                    try {
                        response = postController.getPost(postId);
                        if (response.equals("null")) response = "NO POST!";
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
                String newPost = body.toString();
                JSONObject jsonObject = new JSONObject(newPost);
                if (splittedPath.length == 3) {
                    try {
                        if(!splittedPath[2].equals(LIUDA.getUser())) {
                            response = "NOT ALLOWED!!!";
                            break;
                        }
                        else {
                            try {
                                response = postController.createPost(splittedPath[2], jsonObject.getString("content"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 4) {
                    try {
                        if(!splittedPath[2].equals(LIUDA.getUser())) {
                            response = "NOT ALLOWED!!!";
                            break;
                        }
                        else {
                            try {
                                response = postController.updatePost(splittedPath[3], splittedPath[2], jsonObject.getString("content"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                    response = "NOT A VALID REQUEST!";
                break;
            case "DELETE":
                if (splittedPath.length > 2) {
                    String postId = splittedPath[splittedPath.length - 1];
                    try {
                        response = postController.deletePost(postId);
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
