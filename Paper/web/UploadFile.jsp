<%--
  Created by IntelliJ IDEA.
  User: Billy
  Date: 2016/8/6
  Time: 11:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=gbk" language="java" pageEncoding="gbk" autoFlush="true" %>
<html>
<head>
    <title>论文格式校正</title>
</head>
<body>
<%@ page import="java.io.*,java.util.*, javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.apache.commons.io.output.*" %>

<%
//    String fileSavePath = "C:\\Users\\Billy\\Documents\\GitHub\\Java\\Paper\\data";
    String fileCode=(String)System.getProperties().get("file.encoding");
    File file ;
    int maxFileSize = 5000 * 1024;
    int maxMemSize = 5000 * 1024;
    ServletContext context = pageContext.getServletContext();
//    String filePath = context.getInitParameter("file-upload");
    String filePath = "C:\\Users\\Billy\\Documents\\GitHub\\Java\\Paper\\web\\data\\";
    // 验证上传内容了类型
    String contentType = request.getContentType();
    if ((contentType.indexOf("multipart/form-data") >= 0)) {

        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存中存储文件的最大值
        factory.setSizeThreshold(maxMemSize);
        // 本地存储的数据大于 maxMemSize.
        factory.setRepository(new File("c:\\temp"));

        // 创建一个新的文件上传处理程序
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置最大上传的文件大小
        upload.setSizeMax( maxFileSize );
        try{
            // 解析获取的文件
            List fileItems = upload.parseRequest(request);

            // 处理上传的文件
            Iterator i = fileItems.iterator();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>JSP File upload</title>");
            out.println("</head>");
            out.println("<body>");
            while ( i.hasNext () )
            {
                FileItem fi = (FileItem)i.next();
                if ( !fi.isFormField () )
                {
                    // 获取上传文件的参数
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();//上传文件名
                    fileName = new String (fileName.getBytes(fileCode),fileCode);//按照系统默认编码重新读取文件名
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();
                    // 写入文件
                    String name;//上传文件的短名称
                    if( fileName.lastIndexOf("\\") >= 0 ){
                        name = fileName.substring( fileName.lastIndexOf("\\"));
                        new File(filePath + name).mkdirs();
                        file = new File( filePath + name + "\\", "origin.docx");//上传文件命名为origin
                    }else{
                        name = fileName.substring(fileName.lastIndexOf("\\") + 1);
                        new File(filePath + name).mkdirs();
                        file = new File( filePath + name + "\\", "origin.docx");//上传文件命名为origin
                    }
                    request.setAttribute("fileName", name);//传递文件名参数
                    fi.write(file);
//                    new File(filePath + name + "\\", "check_out.txt").createNewFile();
//                    new File(filePath + name + "\\", "check_out1.txt").createNewFile();
                    out.println("Uploaded Filename: " + filePath +
                            fileName + "<br>");
                }
            }
            out.println("</body>");
            out.println("</html>");
//            response.sendRedirect("uploadResult.jsp");//上传成功，跳转到上传结果界面
        }catch(Exception ex) {
            System.out.println(ex);
        }
    }else{
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet upload</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>No file uploaded</p>");
        out.println("</body>");
        out.println("</html>");
    }
%>
<jsp:forward page="deal.jsp"/>
</body>
</html>
