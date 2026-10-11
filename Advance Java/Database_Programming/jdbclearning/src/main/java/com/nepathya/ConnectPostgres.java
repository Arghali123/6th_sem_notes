package com.nepathya;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectPostgres {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/studentdb";

        String username = "Daenish";
        String password = "Daenish";

        try (Connection connection = DriverManager.getConnection(
                url, username, password)) {

            System.out.println(
                    "Connected to PostgreSQL successfully!");

            System.out.println(
                    "Database: " +
                            connection.getCatalog());

        } catch (SQLException e) {
            System.out.println(
                    "Database connection failed.");

            e.printStackTrace();
        }
    }
}
