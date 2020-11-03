package com.example.bullseye_android.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Random;

public class TempPasswordGenerator {

    public static String getTempPass(int len) throws IOException {
        Document doc = Jsoup.connect("https://www.cs.cmu.edu/~biglou/resources/bad-words.txt").get();
        String[] profane = doc.select("pre").text().split("\n");
        String pass;
        do{
            pass = getEncodedRandom(len);
        }while(checkForProfanity(pass, profane));
        return pass;
    }

    private static String getEncodedRandom(int len){
        String possible = "23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char ch = possible.charAt(random.nextInt(possible.length()));
            str.append(ch);
        }
        return str.toString();
    }

    private static boolean checkForProfanity(String password, String[] profanity){
        for(String word : profanity){
            if (password.toLowerCase().contains(word.toLowerCase())) return true;
        }
        return false;
    }

}
