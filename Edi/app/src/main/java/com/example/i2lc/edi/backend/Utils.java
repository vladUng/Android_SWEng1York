package com.example.i2lc.edi.backend;

/**
 * Created by vlad on 06/04/2017.
 */

public class Utils {

    public static String buildIPAddress(String serverIP, int serverPort) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://");
        stringBuilder.append(serverIP);
        stringBuilder.append(":");
        stringBuilder.append(serverPort);
        stringBuilder.append("/");

        return stringBuilder.toString();
    }
}
