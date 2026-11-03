<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="java.time.LocalDate" %>

<%!
    // Declaration
    private String getCourse() {
        return "Advance Java";
    }
%>

<%
    // Scriptlet
    String studentName = "Daenish";
    int marks = 85;

    String result = marks >= 40 ? "Pass" : "Fail";
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Information</title>
</head>

<body>

    <%-- JSP comment --%>

    <h1>Student Information</h1>

    <p>Name: <%= studentName %></p>

    <p>Course: <%= getCourse() %></p>

    <p>Marks: <%= marks %></p>

    <p>Result: <%= result %></p>

    <p>Date: <%= LocalDate.now() %></p>

</body>
</html>