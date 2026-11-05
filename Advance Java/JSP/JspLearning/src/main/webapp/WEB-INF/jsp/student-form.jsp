<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
 <head>
  <meta charset="UTF-8">
  <title>Student Form</title>
 </head>
 <body>
  <h1>Student Registration</h1>

  <form action="student-result" method="post">
   <label for="name">Name:</label>
   <input type="text" id="name" name="name" required>
   <br><br>

   <label for="email">Email: </label>
   <input type="email" id="email" name="email" required>
   <br><br>

   <label for="course">Course: </label>
   <select id="course" name="course">
    <option value="">Select a course</option>
    <option value="Java">Java</option>
    <option value="Python">Python</option>
    <option value="JavaScript">JavaScript</option>
   </select>

   <p>Select your skills:</p>

   <label>
    <input type="checkbox" name="skills" value="Java">Java
   </label>

   <label>
    <input type="checkbox" name="skills" value="Python">Python
   </label>

   <label>
    <input type="checkbox" name="skills" value="Javascript">JavaScript
   </label>

   <button type="submit">Register</button>

  </form>
 </body>
</html>