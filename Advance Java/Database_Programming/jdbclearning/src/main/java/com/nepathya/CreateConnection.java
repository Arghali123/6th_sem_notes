package com.nepathya;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateConnection {

    public static void main(String[] args)
    {
        String url = "jdbc:postgresql://localhost:5432/";

        String username = "root";
        String password = "";

        try{
            Connection connection= DriverManager.getConnection(url,username,password);

            Statement stmt=connection.createStatement();

            String sqlQuery= """
                    CREATE DATABASE nepathya_6th_bca
                    """;

            stmt.executeUpdate(sqlQuery);
            stmt.close();

            connection.close();
        }catch (SQLException e)
        {

        }
    }
}
