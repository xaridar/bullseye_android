// Elliot wrote
package com.example.bullseye_android.util;

import java.util.regex.Pattern;

public class EmailChecker {

    public static boolean checkEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        if (email.equals("")) {
            return false;
        }
        return pattern.matcher(email).matches();
    }
}
