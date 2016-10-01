package com.billy.SocketWithCShape;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Billy on 2016/10/1.
 */
public class Test {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipInfo = inetAddress.toString();
        System.out.println(ipInfo.substring(ipInfo.indexOf("/") + 1));
    }
}
