<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

  <%
   request.setCharacterEncoding("UTF-8");
   String studentName=request.getParameter("name");
   String studentCourse=request.getParameter("course");
   String email=request.getParameter("email");
   String[] skills=request.getParameterValues("skills");

   String errorMessage=null;

   if(studentName==null || studentName.trim().isEmpty()){
    errorMessage="Student name is required";
   }else if(studentCourse==null || studentCourse.trim().isEmpty()){
    errorMessage="Student course is required";
   }else if(email==null || email.trim().isEmpty()){
    errorMessage="Student email is required";
   }
  %>

<!DOCTYPE html>
<html>
 <head>
  <meta charset="UTF-8">
  <title>Student Result</title>
 </head>

 <body>
  <% if (errorMessage != null) { %>
      <p style="color: red;"><%= errorMessage %></p>
      <a href="student-form">Try again</a>
  <% } else { %>
      <h2>Registration received</h2>
      <p>Name: <%= studentName %></p>
      <p>Email: <%= email %></p>
      <p>Course: <%= studentCourse %></p>
  <% } %>

  <%
    if(skills==null || skills.length == 0){
      out.println("<p>No Skills selected.</p>");
    }
    else{
     for(String skill:skills){
      out.println("<p>Skills: " + skill + "</p>");
     }
    }
  %>

 </body>
</html>

