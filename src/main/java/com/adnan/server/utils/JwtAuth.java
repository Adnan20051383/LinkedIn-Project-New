package com.adnan.server.utils;

public class JwtAuth {
    private static final String secret = "AdnanHeidary2005";
    public static String jws(String sub) {
        char[] arr = new char[sub.length() + 1];
        arr[0] = '@';
        for (int i = 0; i < sub.length(); i++)
            arr[i + 1] = (char)(48 + ((arr[i] + (int)sub.charAt(i) ^ (int)secret.charAt(i % secret.length())) % 50));
        System.out.println(new String(arr));
        return new String(arr);
    }
}
