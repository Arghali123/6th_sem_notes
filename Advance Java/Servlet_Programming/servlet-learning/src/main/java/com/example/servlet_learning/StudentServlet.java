package com.example.servlet_learning;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/student")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException
    {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("GET request: Retrieve student information");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException
    {
        request.setCharacterEncoding("UTF-8");

        String name=request.getParameter("name");

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");


        if(name == null || name.isBlank())
        {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Student name is required"
            );
            return;
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().println("Student received: " + name);
    }
}
