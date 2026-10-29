package com.example.servlet_learning;
import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/submit-student")
public class StudentFormServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        // Set request encoding before reading parameters.
        request.setCharacterEncoding("UTF-8");

        // Read submitted form values.
        String studentName = request.getParameter("studentName");
        String email = request.getParameter("email");
        String course = request.getParameter("course");

        // Validate on the server.
        if (studentName == null || studentName.isBlank()
                || email == null || email.isBlank()
                || course == null || course.isBlank()) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Name, email, and course are required."
            );
            return;
        }

        // Return a simple text response.
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println("Registration received!");
        response.getWriter().println("Student Name: " + studentName);
        response.getWriter().println("Email: " + email);
        response.getWriter().println("Course: " + course);
    }
}

