package com.example.servlet_learning.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = System.getenv().getOrDefault(
            "ADVANCE_DB_URL",
            "jdbc:postgresql://localhost:5432/advance_java_db"
    );

    private static final String USER=System.getenv().getOrDefault(
            "ADVANCE_DB_USER",
            "Daenish"
    );
    private static final String PASSWORD=System.getenv().getOrDefault(
            "ADVANCE_DB_PASSWORD",
            "Daenish"
    );

    public static final Connection getConnection() throws SQLException {

        if(USER == null || PASSWORD == null)
        {
            throw new SQLException("User or Password is null");
        }
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found:");
            e.printStackTrace();
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Database connection failed:");
            System.err.println("URL: " + URL);
            System.err.println("User: " + USER);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
