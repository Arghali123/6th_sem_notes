<%@ page contentType="text/html;charset=UTF-8"
pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
 <head
  <meta charset="UTF-8">
  <title>Student List</title>
 </head>

 <body>
  <h1>Students</h1>
      <table border="1">
          <thead>
              <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Course</th>
              </tr>
          </thead>

          <tbody>
              <c:forEach var="student" items="${requestScope.students}">
                  <tr>
                      <td><c:out value="${student.id}"/></td>
                      <td><c:out value="${student.name}"/></td>
                      <td><c:out value="${student.email}"/></td>
                      <td><c:out value="${student.course}"/></td>
                  </tr>
              </c:forEach>
          </tbody>
      </table>
 </body>
</html>