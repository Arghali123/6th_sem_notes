package com.example.servlet_learning.RequestDispatcher;

import com.example.servlet_learning.Db.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/display-student")
public class DisplayStudentServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException
    {
        Student student=(Student) request.getAttribute("student");
        response.setContentType("text/html;charset=UTF-8");

        if (student == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("Student data not found.");
            return;
        }


        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h2>Student Details</h2>");
        response.getWriter().println(
                "<p>ID: " + student.getId() + "</p>"
        );
        response.getWriter().println(
                "<p>Name: " + escapeHtml(student.getName()) + "</p>"
        );
        response.getWriter().println(
                "<p>Email: " + escapeHtml(student.getEmail()) + "</p>"
        );
        response.getWriter().println(
                "<p>Course: " + escapeHtml(student.getCourse()) + "</p>"
        );
        response.getWriter().println("</body></html>");
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
