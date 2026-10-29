package com.example.servlet_learning;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class InitParameterServlet extends HttpServlet {

    private String defaultCourse;
    private int maxStudents;
    private String appName;
    private String supportEmail;

    @Override
    public void init() throws ServletException {
        //Servlet-specific configuration
        defaultCourse=getServletConfig().getInitParameter("defaultCource");

        String maxStudentValue=getServletConfig().getInitParameter("maxStudents");

        if(defaultCourse == null || maxStudentValue == null)
        {
            throw new ServletException("Required servlet parameters are missing");
        }

        try{
            maxStudents=Integer.parseInt(maxStudentValue);
        }
        catch(NumberFormatException e)
        {
            throw new ServletException("maxStudents must be a valid integer "+e.getMessage());
        }

        if(maxStudents <= 0)
        {
            throw new ServletException("maxStudents must be a positive integer");
        }

        appName=getServletConfig().getInitParameter("appName");
        supportEmail=getServletConfig().getInitParameter("supportEmail");

        if(appName == null || supportEmail == null)
        {
            throw new ServletException("Required application parameters are missing");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");

        try(PrintWriter out=response.getWriter())
        {
            out.println("Application Name: "+appName);
            out.println("Support Email: "+supportEmail);
            out.println("Default Course: "+defaultCourse);
            out.println("Max Students: "+maxStudents);

            out.println();
            out.println("Servlet-specific parameters names:");
            Enumeration<String> names=getServletConfig().getInitParameterNames();

            while(names.hasMoreElements())
            {
                String name=names.nextElement();
                String value=getServletConfig().getInitParameter(name);
                out.println(name+": "+value);
            }

            out.println();
            out.println("Application wide parameters names:");
            Enumeration<String> contextNames=getServletContext().getInitParameterNames();

            while (contextNames.hasMoreElements()) {
                String name = contextNames.nextElement();
                out.println(name + " = "
                        + getServletContext().getInitParameter(name));
            }
        }
    }
}
