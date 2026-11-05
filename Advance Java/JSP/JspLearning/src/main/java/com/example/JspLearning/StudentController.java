package com.example.JspLearning;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/student-details")
public class StudentController extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException {
        String studentName="Daenish";
        String course="Advance Java";

        request.setAttribute("studentName", studentName);
        request.setAttribute("course", course);

        request.getRequestDispatcher("/WEB-INF/jsp/student-details.jsp").forward(request, response);
    }
}
