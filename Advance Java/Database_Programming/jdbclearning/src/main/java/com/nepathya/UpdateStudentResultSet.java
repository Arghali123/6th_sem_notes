package com.nepathya;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class UpdateStudentResultSet {
    public static void main(String[] args) {
        String url="jdbc:postgresql://localhost:5432/studentdb";
        String username="Daenish";
        String password="Daenish";

        String sql="SELECT id, name , marks FROM students ORDER BY id";

        try(
            Connection conn=DriverManager.getConnection(url, username, password);
            Statement st=conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE
            );
            ResultSet rs=st.executeQuery(sql);
        )
        {
          boolean found=false;
          while(rs.next())
          {
            if("Sita".equals(rs.getString("name")))
            {
                rs.updateDouble("marks", 95.0);
                rs.updateRow();

                System.out.println("Sita's marks updated to 95");
                found=true;
                break;
            }
          }

          if(!found)
          {
            System.out.println("Student Sita was not found.");
          }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
