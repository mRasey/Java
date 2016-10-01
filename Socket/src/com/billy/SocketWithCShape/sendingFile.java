package com.billy.SocketWithCShape;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class sendingFile {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), 2333);
//        while (true) {
//            socket = new Socket(InetAddress.getLocalHost(), 2333);
            BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String url = new Scanner(System.in).next();
            File file = new File(url);
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            String readIn = bfr.readLine();
            while (readIn != null) {
                System.out.println(readIn);
                bfw.write(readIn + "\n");
                readIn = bfr.readLine();
            }
            bfw.flush();
            bfr.close();
            bfw.close();
        System.out.println("end sending");
//            socket.getOutputStream().close();
//        }
    }
}
