<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page errorPage="error.jsp" %>

<!DOCTYPE html>
<html>
 <head>
  <title>JSP Exception Handling</title>
 </head>

 <body>
  <h2>Division Calculator</h2>

  <%
   int numerator=100;
   int denominator=0;

   int result=numerator/denominator;
  %>

  <p>Result: <%= result %></p>
 </body>
</html>