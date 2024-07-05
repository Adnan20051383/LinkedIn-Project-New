package com.adnan.server.handlers;

import com.adnan.server.controllers.ContactsController;
import com.adnan.server.controllers.EducationController;
import com.adnan.server.controllers.UserController;
import com.adnan.server.dataAccess.LoggedInUserDataAccess;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;

public class ContactsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = null;
        ContactsController contactsController = null;
        LoggedInUserDataAccess LIUDA = null;

        try {
            userController = new UserController();
            contactsController = new ContactsController();
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
                        response = contactsController.getContacts();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (splittedPath.length == 3) {
                    String userId = splittedPath[splittedPath.length - 1];
                    try {
                        response = contactsController.getContacts(userId);
                    } catch (SQLException | ParseException e) {
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
                                response = contactsController.createContacts(jsonObject.getString("userId"), jsonObject.getString("email"), jsonObject.getString("phoneNumber"), jsonObject.getString("address"), jsonObject.getString("birthday"));
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
                if (splittedPath.length == 3) {
                    try {
                        if (!splittedPath[2].equals(LIUDA.getUser())) {
                            response = "NOT ALLOWED!!!";
                            break;
                        }
                        else {
                            response = contactsController.deleteContacts(splittedPath[2]);
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
