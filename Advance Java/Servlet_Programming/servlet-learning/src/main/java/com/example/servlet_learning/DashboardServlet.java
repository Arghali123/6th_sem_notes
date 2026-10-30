package com.example.servlet_learning;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException
    {
        HttpSession session=request.getSession(false);

        if(session == null || session.getAttribute("username") == null)
        {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Login First"
            );
            return;
        }

        String username=(String) session.getAttribute("username");

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<h1>Welcome, "
                + escapeHtml(username) + "!</h1>");

        out.println("<p>You are logged in.</p>");

        out.println("<a href='"
                + response.encodeURL(
                request.getContextPath() + "/logout"
        )
                + "'>Logout</a>");
    }

    private String escapeHtml(String value) {

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
