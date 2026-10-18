package com.nepathya;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MultipleResultsDemo {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/studentdb";
        String username = "Daenish";
        String password = "Daenish";

        String sql = "SELECT id, name, marks FROM students ORDER BY id";

        try (Connection con = DriverManager.getConnection(url, username, password);
                Statement st = con.createStatement()) {

            boolean hasResultSet = st.execute(sql);

            while (true) {

                if (hasResultSet) {

                    try (ResultSet rs = st.getResultSet()) {
                        System.out.println("--- Result Set ---");

                        while (rs.next()) {
                            System.out.println(
                                    rs.getInt("id") + " | " +
                                            rs.getString("name") + " | " +
                                            rs.getDouble("marks"));
                        }
                    }

                } else {

                    int updateCount = st.getUpdateCount();

                    if (updateCount == -1) {
                        break;
                    }

                    System.out.println(
                            "Rows affected: " + updateCount);
                }

                hasResultSet = st.getMoreResults();
            }

            System.out.println("All available results processed.");

        } catch (SQLException e) {
            System.out.println("Database operation failed.");
            e.printStackTrace();
        }
    }
}
