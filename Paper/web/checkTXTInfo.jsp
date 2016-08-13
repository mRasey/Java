<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.File" %><%--
  Created by IntelliJ IDEA.
  User: Billy
  Date: 2016/8/13
  Time: 20:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>论文格式校正</title>
</head>
<body>
    <%
        String absolutePath = "C:\\Users\\Billy\\Documents\\GitHub\\Java\\Paper\\web\\data\\";
        String dirName = request.getParameter("dirName");
        File txtFile = new File(absolutePath + dirName + "\\check_out.txt");
        FileReader fileReader = new FileReader(txtFile);
        BufferedReader bf = new BufferedReader(fileReader);
        String readLine = bf.readLine();
        while (readLine != null) {
            out.print(readLine);
            out.print("<br>");
            readLine = bf.readLine();
        }
    %>
    <a href="checkWordResult.jsp?dirName=<%=dirName%>">下载Word文档</a>
</body>
</html>
