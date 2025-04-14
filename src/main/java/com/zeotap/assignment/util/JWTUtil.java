package com.zeotap.assignment.util;

public class JWTUtil {

    public static String formatToken(String token) {
        if (token == null || token.isBlank()) return "";
        return "Bearer " + token;
    }
}
