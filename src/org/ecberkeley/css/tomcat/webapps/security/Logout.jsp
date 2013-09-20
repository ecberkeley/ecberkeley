<html>
  <head><title>Login Error</title></head>
  <body>
    
    <p><%
    pageContext.getSession().invalidate();
    response.sendRedirect("/security/");
    %>
    </p>
  </body>
</html>