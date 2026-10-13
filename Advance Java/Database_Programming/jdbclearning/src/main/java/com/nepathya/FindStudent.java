package com.nepathya;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class FindStudent {
    public static void main(String[] args) {
        String url="jdbc:postgresql://localhost:5432/studentdb";
        String username="Daenish";
        String password="Daenish";

        String sql="SELECT id, name, age, course, marks FROM students WHERE id = ?";

        int studentId=2;

        try(
            Connection connection=DriverManager.getConnection(url, username, password);
            PreparedStatement ps=connection.prepareStatement(sql);
        )
        {
          //Set the first parameter
          ps.setInt(1, studentId);

          try(ResultSet rs=ps.executeQuery())
          {
            if(rs.next())
            {
                System.out.println("ID: "+rs.getInt("id"));
                System.out.println("Name: "+rs.getString("name"));
                System.out.println("Age: "+rs.getInt("age"));
               System.out.println("Cource: "+rs.getString("course"));
                System.out.println("Marks: "+rs.getDouble("marks"));
            }else{
                System.out.println("Student not found!");
            }
          }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
