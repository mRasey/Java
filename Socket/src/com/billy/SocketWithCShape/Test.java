package com.billy.SocketWithCShape;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipInfo = inetAddress.toString();
        System.out.println(ipInfo.substring(ipInfo.indexOf("/") + 1));

        String path = Test.class.getResource("").toString();
        System.out.println(path);
        path = path.substring(0, path.lastIndexOf("Socket/"));
        System.out.println(path);
    }
}
