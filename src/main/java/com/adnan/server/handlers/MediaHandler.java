package com.adnan.server.handlers;

import com.adnan.server.controllers.UserController;
import com.adnan.server.utils.ExtractUserAuth;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Collections;

public class MediaHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = null;
        try {
            userController = new UserController();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String response = "";
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        // ip:port/media/userID/mediaName/mediaType
        if (splittedPath.length != 5) {
            response = "NOT A VALID REQUEST";
        } else switch (method) {
            case "GET":
                File file;
                file = new File("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets" + "\\" + splittedPath[3] + "." + splittedPath[4]);
                if (!file.exists()) {
                    response = "no file";
                    break;
                }
                exchange.getResponseHeaders().put("Content-Type", Collections.singletonList(splittedPath[4]));
                exchange.sendResponseHeaders(200, file.length());
                OutputStream outputStream = exchange.getResponseBody();
                Files.copy(file.toPath(), outputStream);
                outputStream.close();
                return;
            case "POST":
                try {
                    if (!userController.userExists(splittedPath[2])) {
                        response = "USER NOT FOUND!";
                        break;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {
                    response = "NOT ALLOWED!";
                    break;
                }
                System.out.println("kkkkkkk");
//                Files.copy(exchange.getRequestBody(), Path("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\src\\main\\java\\com\\adnan\\server\\" , splittedPath[2], splittedPath[3] + "." + splittedPath[4]), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("llllll");
                response = "successful!";
                break;
            default:
                response = "NOT A VALID REQUEST";
                break;
        }
        System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
