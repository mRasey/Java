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
    <title>���ڴ���</title>
</head>
<body>
    ���ڴ�������
</body>
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
    // ��֤�ϴ�����������
    String contentType = request.getContentType();
    if ((contentType.indexOf("multipart/form-data") >= 0)) {

        DiskFileItemFactory factory = new DiskFileItemFactory();
        // �����ڴ��д洢�ļ������ֵ
        factory.setSizeThreshold(maxMemSize);
        // ���ش洢�����ݴ��� maxMemSize.
        factory.setRepository(new File("c:\\temp"));

        // ����һ���µ��ļ��ϴ��������
        ServletFileUpload upload = new ServletFileUpload(factory);
        // ��������ϴ����ļ���С
        upload.setSizeMax( maxFileSize );
        try{
            // ������ȡ���ļ�
            List fileItems = upload.parseRequest(request);

            // �����ϴ����ļ�
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
                    // ��ȡ�ϴ��ļ��Ĳ���
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();//�ϴ��ļ���
                    fileName = new String (fileName.getBytes(fileCode),fileCode);//����ϵͳĬ�ϱ������¶�ȡ�ļ���
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();
                    // д���ļ�
                    String name;//�ϴ��ļ��Ķ�����
                    if( fileName.lastIndexOf("\\") >= 0 ){
                        name = fileName.substring( fileName.lastIndexOf("\\"));
                        new File(filePath + name).mkdirs();
                        file = new File( filePath + name + "\\", "origin.docx");//�ϴ��ļ�����Ϊorigin
                    }else{
                        name = fileName.substring(fileName.lastIndexOf("\\") + 1);
                        new File(filePath + name).mkdirs();
                        file = new File( filePath + name + "\\", "origin.docx");//�ϴ��ļ�����Ϊorigin
                    }
                    request.setAttribute("fileName", name);//�����ļ�������
                    fi.write(file);
//                    new File(filePath + name + "\\", "check_out.txt").createNewFile();
//                    new File(filePath + name + "\\", "check_out1.txt").createNewFile();
                    out.println("Uploaded Filename: " + filePath +
                            fileName + "<br>");
                }
            }
            out.println("</body>");
            out.println("</html>");
//            response.sendRedirect("uploadResult.jsp");//�ϴ��ɹ�����ת���ϴ��������
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
</html>
