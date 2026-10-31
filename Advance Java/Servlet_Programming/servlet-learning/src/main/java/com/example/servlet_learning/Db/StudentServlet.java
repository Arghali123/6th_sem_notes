package com.example.servlet_learning.Db;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
    }

    //Display all students
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try
        {
            List<Student> students=studentDAO.getAllStudents();
            PrintWriter out=response.getWriter();

            out.println("<html>");
            out.println("<body>");
            out.println("<h1>Students Records</h1>");

            out.println("<table border='1'>");

            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Name</th>");
            out.println("<th>Email</th>");
            out.println("<th>Course</th>");
            out.println("</tr>");

            for(Student student:students)
            {
                out.println("<tr>");
                out.println("<td>"
                        + student.getId() + "</td>");

                out.println("<td>"
                        + escapeHtml(student.getName())
                        + "</td>");

                out.println("<td>"
                        + escapeHtml(student.getEmail())
                        + "</td>");

                out.println("<td>"
                        + escapeHtml(student.getCourse())
                        + "</td>");

                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");

        }catch (SQLException e)
        {
            getServletContext().log("Unable to retrieve students", e);
            e.printStackTrace();
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to retrieve students from database: " + e.getMessage()
            );
        }
    }

    // Insert a new student
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String course = request.getParameter("course");

        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || course == null || course.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "All fields are required."
            );

            return;
        }

        Student student =
                new Student(name.trim(),
                        email.trim(),
                        course.trim());

        try {

            boolean inserted =
                    studentDAO.insertStudent(student);

            if (inserted) {

                // Post/Redirect/Get pattern
                response.sendRedirect(
                        request.getContextPath() + "/students"
                );


            } else {

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Student registration failed."
                );
            }

        } catch (SQLException e) {

            getServletContext().log(
                    "Unable to insert student", e
            );

            // PostgreSQL SQLSTATE 23505 = unique violation
            if ("23505".equals(e.getSQLState())) {

                response.sendError(
                        HttpServletResponse.SC_CONFLICT,
                        "A student with this email already exists."
                );

            } else {

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Database operation failed."
                );
            }
        }
    }


    // Basic HTML escaping for displayed database values
    private String escapeHtml(String value) {

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
