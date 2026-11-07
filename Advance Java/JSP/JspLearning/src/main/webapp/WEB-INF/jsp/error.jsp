<%@ page contentType="text/html; charset=UTF-8"
isErrorPage="true" %>

<!DOCTYPE html>
<html>
 <head>
  <title>Error</title>
 </head>

 <body>
  <h2>Sorry,Something went wrong.</h2>
  <p>You request could not be completed.</p>

  <%
   if(exception != null)
   {
     application.log("Exception occured in calculate.jsp",exception);
   }
  %>
 </body>
</html>