package com.example.servlet_learning;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/set-cookie")
public class SetCookieServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException
    {
        Cookie cookie=new Cookie("studentName","Daenish");

        cookie.setMaxAge(60*60);

        String contextPath=request.getContextPath();
        cookie.setPath(
                contextPath.isBlank() ? "/":contextPath
        );

        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());

        cookie.setAttribute("SameSite","Lax");

        response.addCookie(cookie);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out=response.getWriter();

        out.println("<h2>Cookie Created Successfully!</h2>");
        out.println("<p>Name: studentName</p>");
        out.println("<p>Value: Daenish</p>");
        out.println("<p>Now visit /read-cookie</p>");
    }
}
