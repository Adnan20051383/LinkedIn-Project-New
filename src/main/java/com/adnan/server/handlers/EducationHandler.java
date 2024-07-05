package com.adnan.server.handlers;

import com.adnan.server.controllers.EducationController;
import com.adnan.server.controllers.UserController;
import com.adnan.server.dataAccess.LoggedInUserDataAccess;
import com.adnan.server.models.Education;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;

public class EducationHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = null;
        EducationController educationController = null;
        LoggedInUserDataAccess LIUDA = null;

        try {
            userController = new UserController();
            educationController = new EducationController();
            LIUDA = new LoggedInUserDataAccess();
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
                        response = educationController.getEducations();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 3) {
                    String userId = splittedPath[splittedPath.length - 1];
                    try {
                        response = educationController.getEducation(userId);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                    response = "NOT A VALID REQUEST!";
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
                            String newEducation = body.toString();
                            JSONObject jsonObject = new JSONObject(newEducation);
                            try {
                                response = educationController.createEducation(jsonObject.getString("userId"), jsonObject.getString("institutionName"), jsonObject.getString("major"), jsonObject.getString("startDate"), jsonObject.getString("endDate"), jsonObject.getDouble("grade"), jsonObject.getString("activities"), jsonObject.getString("extraExplanation"));
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
                if (splittedPath.length == 4) {
                    try {
                        if (!splittedPath[2].equals(LIUDA.getUser())) {
                            response = "NOT ALLOWED!!!";
                            break;
                        }
                        else {
                            response = educationController.deleteEducation(splittedPath[2], java.sql.Date.valueOf(splittedPath[3]));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                    response = "NOT A VALID REQUEST!";

        }

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        }

    }
