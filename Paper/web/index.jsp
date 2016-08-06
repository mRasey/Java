<%--
  Created by IntelliJ IDEA.
  User: Billy
  Date: 2016/8/6
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=gbk" language="java" %>
<html>
  <head>
    <title>论文格式校正</title>
  </head>
  <script language="JavaScript">
      function checkFileType() {
          var fileName = document.getElementById("file").value;
          var postfix = fileName.substring(fileName.length - 5, fileName.length);
          if(postfix == ".docx") {
              return true;
          }
          else {
              alert("请输入有效的Word文件" + fileName);
              return false;
          }
      }
  </script>
  <body>
  <div align="center">
      <form onsubmit="return checkFileType()" method="post" action="UploadFile.jsp" enctype="multipart/form-data">
          <input type="file" name="file" id="file"><br />
          <input type="submit" value="提交">
      </form>
  </div>
  </body>
</html>
