package com.nepathya;

import java.sql.SQLException;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

public class JdbcRowsetExample {
    public static void main(String[] args) {
        String url="jdbc:postgresql://localhost:5432/studentdb";
        String username="Daenish";
        String password="Daenish";

        try(
          JdbcRowSet rowSet=RowSetProvider.newFactory().createJdbcRowSet() )
        {
          rowSet.setUrl(url);
          rowSet.setUsername(username);
          rowSet.setPassword(password);
          rowSet.setCommand("SELECT id, name, marks FROM students ORDER BY id");
          rowSet.execute();

          if(rowSet.last())
          {
            System.out.println("Last student :"+rowSet.getString("name"));
            System.out.println("Marks :"+rowSet.getString("marks"));
          }else
          {
            System.out.println("No students found");
          }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
