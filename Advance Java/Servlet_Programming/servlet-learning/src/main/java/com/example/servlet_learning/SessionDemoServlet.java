package com.example.servlet_learning;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/session-demo")
public class SessionDemoServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException
    {
        HttpSession session=request.getSession();

        Integer count=(Integer) session.getAttribute("visitCount");

        if(count == null)
        {
            count=0;
        }

        count++;

        session.setAttribute("visitCount", count);

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("Visits in this session: "+count);
    }
}
