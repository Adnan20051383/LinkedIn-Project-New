package com.adnan.server.dataAccess;

import java.rmi.ConnectIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainDataBase {
    private static final String SQLURL = "jdbc:mysql://localhost:3306/usersdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "a1d3n8a3n";

    private static Connection connection;

    private MainDataBase () {

    }
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed())
            connection = DriverManager.getConnection(SQLURL, USERNAME, PASSWORD);
        return connection;
    }


}
