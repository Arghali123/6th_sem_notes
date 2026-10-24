package com.example.servlet_learning;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(
        value = "/lifecycle",
        loadOnStartup = 1
)
public class ServletLifecycleDemo extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("1. init() : Servlet is initialized");
    }

    @Override
    protected  void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException
    {
        System.out.println("2. doGet() : Request received");

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("Servlet Lifecycle Demo");
        response.getWriter().println("The servlet is handling a GET request");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException
    {
        System.out.println("2. doPost() : Request received");

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("The servlet is handling a POST request");
    }

    @Override
    public void destroy() {
        System.out.println("3. destroy() : Servlet is sutting down  ");
    }
}
