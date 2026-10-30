package com.example.servlet_learning;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException
    {
        String username=request.getParameter("username");
        String password=request.getParameter("password");

        if("David".equals(username) && "java123".equals(password))
        {
            HttpSession existingSession=request.getSession(false);

            if(existingSession != null)
            {
                existingSession.invalidate();
            }

            HttpSession session=request.getSession();
            session.setAttribute("username", username);

            session.setMaxInactiveInterval(15*60);

            response.sendRedirect(request.getContextPath()+"/dashboard");
        }else
        {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid username or password"
            );
        }

    }
}
