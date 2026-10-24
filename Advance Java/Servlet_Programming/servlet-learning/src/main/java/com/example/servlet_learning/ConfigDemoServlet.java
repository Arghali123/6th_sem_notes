package com.example.servlet_learning;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(
     value="/config-demo"   ,
        initParams = {
             @jakarta.servlet.annotation.WebInitParam(name="courseName", value="Advance Java")
        }
)
public class ConfigDemoServlet extends HttpServlet {

    String courseName;

    @Override
    public void init() {
        ServletConfig config=getServletConfig();
        courseName=config.getInitParameter("courseName");
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException
    {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("Course Name: " + courseName);
    }
}
