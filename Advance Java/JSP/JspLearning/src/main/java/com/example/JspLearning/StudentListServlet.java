package com.example.JspLearning;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/students")
public class StudentListServlet extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init()
    {
        studentDAO=new StudentDAO();
    }

    @Override
    protected void doGet(
           HttpServletRequest request,
           HttpServletResponse response
    ) throws IOException, ServletException
    {
        try
        {
            List<Student> students=studentDAO.findAll();
            request.setAttribute("students",students);
            request.getRequestDispatcher("/students.jsp").forward(request,response);
        }catch (SQLException exception)
        {
            getServletContext().log("Unable to retrieve students from the database",exception);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
