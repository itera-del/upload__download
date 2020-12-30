
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
  <head>
    <title>页面</title>
  </head>
  <body>
  <form action="${pageContext.request.contextPath}/uploadServlet" method="post" enctype="multipart/form-data">
    <input type="file" name="文件">
    <br>
    <input type="submit" value="提交">
  </form>
  <a href="${pageContext.request.contextPath}/downloadServlet" >下载</a>
  </body>
</html>
