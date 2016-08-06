<%--
  Created by IntelliJ IDEA.
  User: Billy
  Date: 2016/8/6
  Time: 13:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.lang.*" %>
<%@ page language="java" import="java.io.*" %>
<html>
<head>
    <title>论文格式修改</title>
</head>
<body>
    <div>
        正在处理<br>
    </div>
</body>
<%
    try{
        System.out.println("start");
        Process pr = Runtime.getRuntime().exec("python test.py");

        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
        pr.waitFor();
        System.out.println("end");
    } catch (Exception e){
        e.printStackTrace();
    }
%>
</html>
