package com.billy.SocketWithCShape;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class receiveFile {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2333, 100);

        while(true) {
            System.out.println("waiting");
            Socket socket = serverSocket.accept();
            System.out.println("start sending");
            File file = new File("C:/Users/Billy/Desktop/copy.txt");
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bfw = new BufferedWriter(new FileWriter(file));
            String readIn = bfr.readLine();
            while(readIn != null) {
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
    }
}
