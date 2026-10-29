package com.example.servlet_learning;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

@WebServlet("/read-all-parameters")
public class ReadAllParametersServlet extends HttpServlet {

    @Override
    protected void doPost(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response
    ) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");

        response.setContentType("text/plain;charset=UTF-8");

        try(PrintWriter out = response.getWriter())
        {
            out.println("=== Using getParameterNames() ===");

            Enumeration<String> names = request.getParameterNames();

            while(names.hasMoreElements())
            {
                String name = names.nextElement();

                String[] values=request.getParameterValues(name);

                out.println(name + " = "
                        + String.join(", ", values));
            }

            out.println();

            out.println("=== Using getParameterMap() ===");
            Map<String,String[]> parameterMap = request.getParameterMap();

            for(Map.Entry<String,String[]> entry: parameterMap.entrySet())
            {
                String name = entry.getKey();
                String[] values = entry.getValue();

                out.println(name + " = "
                        + String.join(", ", values));
            }
        }
    }
}
