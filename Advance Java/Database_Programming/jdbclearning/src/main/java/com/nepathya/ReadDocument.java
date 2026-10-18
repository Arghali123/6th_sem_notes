package com.nepathya;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadDocument {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/studentdb";
        String username = "Daenish";
        String password = "Daenish";

        // The file name we stored in the database earlier
        String targetFileName = "hello.pdf";
        String sql = "SELECT file_data FROM documents WHERE file_name = ?";

        try (
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, targetFileName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the binary stream from the database
                    try (InputStream input = rs.getBinaryStream("file_data")) {
                        
                        // Define where you want to save the retrieved file
                        File outputFile = new File("output_" + targetFileName);
                        
                        try (FileOutputStream output = new FileOutputStream(outputFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            
                            while ((bytesRead = input.read(buffer)) != -1) {
                                output.write(buffer, 0, bytesRead);
                            }
                        }
                    }

                    System.out.println("Document successfully read from database and saved as: output_" + targetFileName);
                } else {
                    System.out.println("No document found with the name: " + targetFileName);
                }
            }

        } catch (SQLException | IOException e) {
            System.out.println("Could not read the document from the database.");
            e.printStackTrace();
        }
    }
}