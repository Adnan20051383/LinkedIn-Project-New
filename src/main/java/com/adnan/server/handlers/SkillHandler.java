package com.adnan.server.handlers;

import com.adnan.server.controllers.PostController;
import com.adnan.server.controllers.SkillController;
import com.adnan.server.controllers.UserController;
import com.adnan.server.dataAccess.LoggedInUserDataAccess;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class SkillHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SkillController skillController = null;
        UserController userController = null;
        LoggedInUserDataAccess LIUDA = null;
        try {
            skillController = new SkillController();
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
                if (splittedPath.length == 2) {
                    try {
                        response = skillController.getSkills();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else if (splittedPath.length == 3) {
                    String userId = splittedPath[splittedPath.length - 1];
                    try {
                        response = skillController.getSkills(userId);
                        if (response.equals("null")) response = "NO SKILL!";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else
                    response = "NOT A VALID REQUEST!!!";
                break;
            case "POST":
                if (splittedPath.length == 3) {
                    try {
                        if(!splittedPath[2].equals(LIUDA.getUser())) {
                            response = "NOT ALLOWED!!!";
                            break;
                        }
                        else {
                            InputStream requestBody = exchange.getRequestBody();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                            StringBuilder body = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                body.append(line);
                            }
                            requestBody.close();
                            String newSkill = body.toString();
                            JSONObject jsonObject = new JSONObject(newSkill);
                            try {
                                response = skillController.createSkill(jsonObject.getString("userId"), jsonObject.getString("text"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(splittedPath.length == 4) {
                    try {
                        if(!splittedPath[2].equals(LIUDA.getUser())) {
                            response = "NOT ALLOWED!!!";
                            break;
                        }
                        else {
                            InputStream requestBody = exchange.getRequestBody();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                            StringBuilder body = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                body.append(line);
                            }
                            requestBody.close();
                            String newSkill = body.toString();
                            JSONObject jsonObject = new JSONObject(newSkill);
                            try {
                                response = skillController.updateSkill(jsonObject.getString("userId"), Integer.parseInt(splittedPath[splittedPath.length - 1]) - 1, jsonObject.getString("text"));
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
                    String userId = splittedPath[splittedPath.length - 2];
                    int nthSkill = Integer.parseInt(splittedPath[splittedPath.length - 1]);
                    try {
                        response = skillController.deleteSkill(userId, nthSkill);
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
