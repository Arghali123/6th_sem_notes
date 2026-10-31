package com.example.servlet_learning.Db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Insert a student into PostgreSQL
    public boolean insertStudent(Student student)
            throws SQLException {

        String sql = """
                INSERT INTO students (name, email, course)
                VALUES (?, ?, ?)
                """;

        try (Connection connection =
                     DbConnection.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getCourse());

            int rowsInserted = ps.executeUpdate();

            return rowsInserted > 0;
        }
    }

    // Retrieve all students from PostgreSQL
    public List<Student> getAllStudents()
            throws SQLException {

        String sql = """
                SELECT id, name, email, course
                FROM students
                ORDER BY id
                """;

        List<Student> students = new ArrayList<>();

        try (Connection connection =
                     DbConnection.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql);

             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Student student = new Student(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("course")
                );

                students.add(student);
            }
        }

        return students;
    }
}