package com.billy.SocketWithCShape;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(2333, 100);
        while(true) {
            System.out.println("waiting");
            Socket clientSocket = serverSocket.accept();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String userInfo = bfr.readLine();
            String[] infos = userInfo.split(" ");
            switch (infos[0]) {
                case "register":
                    new Thread(new OperateSQL(infos[0], infos[1], infos[2], infos[3], clientSocket)).start();
                    break;
                case "signIn":
                    new Thread(new OperateSQL(infos[0], infos[1], infos[2], clientSocket)).start();
                    break;
                case "updateIpAddress":
                    new Thread(new OperateSQL(infos[0], infos[1], clientSocket)).start();
            }
        }
    }
}
