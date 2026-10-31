package com.example.servlet_learning.RequestDispatcher;

import com.example.servlet_learning.Db.Student;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/students-details")
public class StudentDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException
    {
        //In a real application, retrieve this from your DAO.
        Student student=new Student(1,"Asha Sharma","asha@gmail.com","Advance Java");

        //Store the Java object in the request.
        request.setAttribute("student", student);

        //Forward the same request to another servlet
        RequestDispatcher dispatcher=request.getRequestDispatcher("/display-student");

        dispatcher.forward(request,response);
    }
}
