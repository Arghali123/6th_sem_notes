package com.nepathya;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPoolDemo {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/studentdb";
        String username = "Daenish";
        String password = "Daenish";

        HikariConfig config=new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        //Pool configuration
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(3000);

        try(HikariDataSource dataSource=new HikariDataSource(config))
        {
           try(Connection conn=dataSource.getConnection();
           Statement st=conn.createStatement();
           ResultSet rs=st.executeQuery(
            "SELECT current_database()"
           )

              )
              {
                if(rs.next())
                {
                    System.out.println("Connected to: "+rs.getString(1));
                }
              }
        }catch(Exception e)
        {
            System.out.println("Database operation failed");
            e.printStackTrace();
        }
    }
}
