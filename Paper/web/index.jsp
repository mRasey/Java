<%--
  Created by IntelliJ IDEA.
  User: Billy
  Date: 2016/8/6
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>论文格式校正</title>
  </head>
  <script type="javascript">
      function checkFileType() {
          alert("请输入有效的Word文件");
          return false;
      }
  </script>
  <body>
  <div align="center">
      <form onsubmit="return checkFileType()" method="post" action="UploadFile.jsp" enctype="multipart/form-data">
          <input type="file" name="file"><br />
          <input type="submit" value="提交">
      </form>
  </div>
  </body>
</html>
