// Elliot wrote
package com.example.bullseye_android.util;

public class TimeFormatter {
    public static String[] formatTime (long seconds) {
        long mins = (seconds - (seconds % 60)) / 60;
        long secs = seconds % 60;
        String secStr = "";
        String minStr = "";
        if (secs < 10) {
            secStr = "0";
        }
        secStr+=(secs + "");
        if (mins < 10) {
            minStr = "0";
        }
        minStr+=(mins + "");
        return new String[] {minStr, secStr};
    }
}
