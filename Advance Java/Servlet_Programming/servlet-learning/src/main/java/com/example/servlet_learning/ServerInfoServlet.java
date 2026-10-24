package com.example.servlet_learning;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/server-info")
public class ServerInfoServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException
    {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String json = """
                {
                    "application": "Student Management System",
                    "backend": "Java Servlet",
                    "server": "Apache Tomcat",
                    "message": "Request processed successfully"
                }
                """;

        response.getWriter().write(json);
    }
}
