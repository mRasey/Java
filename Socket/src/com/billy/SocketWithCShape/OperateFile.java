package com.billy.SocketWithCShape;

import java.io.*;
import java.net.Socket;

public class OperateFile implements Runnable{

    private String operation;
    private String url;
    private Socket socket;
    private String path;
    private String fileName;
    private String userName;

    public OperateFile(String operation, String userName, String url, Socket socket) {
        this.operation = operation;
        this.url = url;
        this.socket = socket;
        this.userName = userName;
        this.fileName = url.substring(url.lastIndexOf("/") + 1);

        path = OperateSQL.class.getResource("").toString();
        path = path.substring(0, path.lastIndexOf("Socket/"));
        path = path.substring(path.indexOf("/") + 1) + "/data/" + userName + "/diary/" + fileName;
    }

    public void receiveFile() throws IOException {
        System.out.println("start sending");
        BufferedWriter bfw = new BufferedWriter(new FileWriter(new File(path)));
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String readIn = bfr.readLine();
        while(!readIn.equals("end sending")) {
            System.out.println(readIn);
            bfw.write(readIn);
            bfw.newLine();
            readIn = bfr.readLine();
        }
        bfw.flush();
        bfw.close();
        bfr.close();
        System.out.println("end receiving");
    }

    @Override
    public void run() {
        try {
            switch (operation) {
                case "sendFile":
                    receiveFile();
                    break;
                default:
                    break;
            }
        }catch (IOException e) {

        }
    }
}
