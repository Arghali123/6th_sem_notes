package com.nepathya;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchStudentsByCource {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/studentdb";
        String username = "Daenish";
        String password = "Daenish";

        String sql = "SELECT name, marks FROM students WHERE course = ?";
        String cource = "Java";

        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement ps = connection.prepareStatement(sql);) {
            // Set the first parameter
            ps.setString(1, cource);

            try (ResultSet rs = ps.executeQuery()) {
                 boolean found=false;

                 while(rs.next())
                 {
                    found=true;
                    System.out.println("Name: "+rs.getString("name"));
                    System.out.println("Marks: "+rs.getDouble("marks"));
                    System.out.println("-------------------");
                 }

                 if(!found)
                 {
                    System.out.println("Students not found");
                 }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
