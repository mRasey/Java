package com.billy.SocketWithCShape;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Send {
    public static void main(String[] args) throws IOException, InterruptedException {

        while(true) {
            String info = new Scanner(System.in).nextLine();
            Socket socket = new Socket(InetAddress.getLocalHost(), 2333);

            BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("sending");
            bfw.write(info + "\n");
            bfw.flush();

            System.out.println(bfr.readLine());
        }
    }
}
