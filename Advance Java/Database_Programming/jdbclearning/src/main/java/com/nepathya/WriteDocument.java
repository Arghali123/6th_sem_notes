package com.nepathya;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WriteDocument {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/studentdb";
        String username = "Daenish";
        String password = "Daenish";

        String fileName = "hello.pdf";
        String sql = "INSERT INTO documents (file_name, file_data) VALUES (?, ?)";

        // Load hello.pdf directly from the same package folder using getResourceAsStream
        try (
            Connection conn = DriverManager.getConnection(url, username, password);
            InputStream input = WriteDocument.class.getResourceAsStream(fileName);
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            // Check if the file was successfully found in the classpath
            if (input == null) {
                System.out.println("File not found in classpath: " + fileName);
                return;
            }

            ps.setString(1, fileName);
            ps.setBinaryStream(2, input); // Streams the file bytes directly into the database

            int rows = ps.executeUpdate();

            System.out.println(rows == 1
                    ? "Document saved successfully."
                    : "Document was not saved.");
        } catch (SQLException | IOException e) {
            System.out.println("Could not save the document.");
            e.printStackTrace();
        }
    }
}