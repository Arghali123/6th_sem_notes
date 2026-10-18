package com.nepathya;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScrollableStudents {
    public static void main(String[] args) {
        String url="jdbc:postgresql://localhost:5432/studentdb";
        String username="Daenish";
        String password="Daenish";

        String sql="SELECT id, name, marks FROM students ORDER BY id";

        try(
            Connection conn=DriverManager.getConnection(url, username, password);

            Statement st=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs=st.executeQuery(sql)
        )
        {
          //Move to the first row
          if(rs.first())
          {
            System.out.println("First Student: "+rs.getString("name"));
          }

          //Move directly to the last row
          if(rs.last())
          {
            System.out.println("Last Student: "+rs.getString("name"));
          }

          //Move directly to row number 2
          if(rs.absolute(2))
          {
            System.out.println("Student at row 2: "+rs.getString("name"));
          }

          //Move backword one row
          if(rs.previous())
          {
            System.out.println("Previous Student: "+rs.getString("name"));
          }
        }catch(SQLException e)
        {
         e.printStackTrace();
        }
    }
}
