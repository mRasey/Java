package com.billy.SocketWithCShape;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.*;

public class OperateSQL implements Runnable{

    private String method;
    private String name;
    private String password;
    private String ipAddress;
    private String email;
    private Socket clientSocket;
    private Connection connection;
    private Statement statement;
    private String sql;
    private BufferedWriter bfw;

    public OperateSQL(String method, String name, Socket clientSocket) throws ClassNotFoundException, SQLException, IOException {
        this.method = method;
        this.name = name;
        this.clientSocket = clientSocket;
        this.bfw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String ipAddress = clientSocket.getInetAddress().toString();
        this.ipAddress = ipAddress.substring(ipAddress.indexOf("/") + 1);

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/diary?user=root&password=199507wz";
        connection = DriverManager.getConnection(url);
        if(connection.isClosed()) {
            throw new SQLException("连接数据库失败");
        }
        System.out.println("连接数据库成功");
        statement = connection.createStatement();
    }

    public OperateSQL(String method, String name, String password, Socket clientSocket) throws ClassNotFoundException, SQLException, IOException {
        this.method = method;
        this.name = name;
        this.password = password;
        this.clientSocket = clientSocket;
        this.bfw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String ipAddress = clientSocket.getInetAddress().toString();
        this.ipAddress = ipAddress.substring(ipAddress.indexOf("/") + 1);

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/diary?user=root&password=199507wz";
        connection = DriverManager.getConnection(url);
        if(connection.isClosed()) {
            throw new SQLException("连接数据库失败");
        }
        System.out.println("连接数据库成功");
        statement = connection.createStatement();
    }

    public OperateSQL(String method, String name, String password, String email, Socket clientSocket) throws ClassNotFoundException, SQLException, IOException {
        this.method = method;
        this.name = name;
        this.password = password;
        this.email = email;
        this.clientSocket = clientSocket;
        this.bfw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String ipAddress = clientSocket.getInetAddress().toString();
        this.ipAddress = ipAddress.substring(ipAddress.indexOf("/") + 1);

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/diary?user=root&password=199507wz";
        connection = DriverManager.getConnection(url);
        if(connection.isClosed()) {
            throw new SQLException("连接数据库失败");
        }
        System.out.println("连接数据库成功");
        statement = connection.createStatement();
    }


    public void signIn() throws SQLException, IOException {
        sql = "SELECT name, password FROM users_info WHERE name = " + $(name);
        ResultSet resultSet = statement.executeQuery(sql);
        if(resultSet.next()) {
            String password = resultSet.getString("password");
            if(this.password.equals(password)) {
                bfw.write("signIn success\n");
                bfw.flush();
                printInfo("signIn success");
                return;
            }
            bfw.write("error password\n");
            bfw.flush();
            printInfo("error password");
            return;
        }
        bfw.write("error account\n");
        bfw.flush();
        printInfo("error account");
    }

    public void register() throws SQLException, IOException {
        sql = "SELECT name, password FROM users_info WHERE name = " + $(name);
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            bfw.write("account already exist\n");
            bfw.flush();
            printInfo("account already exist");
            return;
        }
        sql = "INSERT " +
                "INTO users_info " +
                "VALUES (" + $(name) + ","
                + $(password) + ","
                + $(email) + ","
                + $(ipAddress) + ");";
        statement.executeUpdate(sql);
        bfw.write("register success\n");
        bfw.flush();
        String path = OperateSQL.class.getResource("").toString();
        path = path.substring(0, path.lastIndexOf("Socket/"));
        path = path.substring(path.indexOf("/") + 1);
        new File(path + "/data/" + name + "/diary").mkdirs();
        printInfo("register success");
    }

    public void updateIpAddress() throws SQLException, IOException {
        sql = "UPDATE users_info " +
                "SET ipAddress=" + $(ipAddress) + " " +
                "WHERE name=" + $(name) + ";";
        statement.executeUpdate(sql);
        bfw.write("update ipAddress success\n");
        bfw.flush();
        printInfo("update ipAddress success");
    }

    private String $(String s) {
        return "'" + s + "'";
    }

    private void printInfo(String s) {
        System.out.println(ipAddress + " " + s);
    }
    @Override
    public void run() {
        try {
            switch (method) {
                case "signIn":
                    signIn();
                    break;
                case "register":
                    register();
                    break;
                case "updateIpAddress":
                    updateIpAddress();
                    break;
                default:
                    System.out.println("error");
                    break;
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
