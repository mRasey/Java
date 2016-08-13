<%--
  Created by IntelliJ IDEA.
  User: Billy
  Date: 2016/8/13
  Time: 17:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@page import="java.io.*"%>

<%@ page language="java" contentType="text/html; charset=gbk"
         pageEncoding="UTF-8"%>
<%
    String fileName = (String) request.getAttribute("fileName");
    String filepath = request.getParameter("filepath");//"路径";
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment;filename = " + fileName);
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath + fileName), "utf-8"));
    BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "utf-8"));
    String line;
    while((line=br.readLine())!=null){
        fos.write(line+"\n\r");
    }
    fos.flush();
    fos.close();
    br.close();
    out.clear();
    out = pageContext.pushBody();
%>


<a href="" >下载</a>
</body>
</html>
