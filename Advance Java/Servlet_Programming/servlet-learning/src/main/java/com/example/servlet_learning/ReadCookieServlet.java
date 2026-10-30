package com.example.servlet_learning;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/read-cookie")
public class ReadCookieServlet extends HttpServlet {

    @Override
    protected void doGet(
       HttpServletRequest request,
       HttpServletResponse response
    ) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out=response.getWriter();

        //Get Cookie send by the browser
        Cookie[] cookies=request.getCookies();

        if(cookies != null)
        {
            boolean found=false;

            for(Cookie cookie: cookies)
            {
                if("studentName".equals(cookie.getName()))
                {
                    out.println("<h2>Cookie Found</h2>");
                    out.println("<p>Student Name: " + cookie.getValue() + "</p>");
                    found=true;
                    break;
                }
            }

            if(!found)
            {
                out.println("<h2>studentName Cookie Not Found</h2>");
            }
        }else {
            out.println("<h2>No cookies received</h2>");
        }
    }
}
