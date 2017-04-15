package com.example.i2lc.edi.backend;

import java.util.regex.Pattern;

/**
 * Created by vlad on 06/04/2017.
 */

public class Utils {
    static final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip) { return PATTERN.matcher(ip).matches(); }

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
