<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
 <head>
  <meta charset="UTF-8">
  <title>Student Details</title>
 </head>

 <body>
   <h1>Student Details</h1>
   <p>Name: ${requestScope.studentName}</p>
   <p>Course: ${requestScope.course}</p>
 </body>
</html>