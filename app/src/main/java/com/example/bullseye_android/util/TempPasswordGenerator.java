package com.example.bullseye_android.util;

import java.util.Random;

public class TempPasswordGenerator {

    public static String getEncodedRandom(int len){
        String possible = "23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char ch = possible.charAt(random.nextInt(possible.length()));
            str.append(ch);
        }
        return str.toString();
    }

}
