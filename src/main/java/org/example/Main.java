package org.example;

import com.adnan.server.handlers.*;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/users", new UserHandler());
        server.createContext("/bios", new BioHandler());
        server.createContext("/posts", new PostHandler());
        server.createContext("/session", new SessionHandler());
        server.createContext("/educations", new EducationHandler());
        server.createContext("/contacts", new ContactsHandler());
        server.createContext("/skills", new SkillHandler());
        server.createContext("/follows", new FollowHandler());
        server.createContext("/connects", new ConnectHandler());
        server.createContext("/likes", new LikeHandler());
        server.createContext("/comments", new CommentHandler());
        server.createContext("/messages", new MessageHandler());
        server.createContext("/tags", new HashtagHandler());
        server.createContext("/media", new MediaHandler());
        server.start();
    }

    }