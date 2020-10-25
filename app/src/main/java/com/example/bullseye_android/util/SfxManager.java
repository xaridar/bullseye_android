package com.example.bullseye_android.util;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class SfxManager {

    private static ArrayList<MediaPlayer> sounds = new ArrayList<>();

    public static MediaPlayer createSfx(Context context, int resid, float volume) {
        MediaPlayer player = MediaPlayer.create(context, resid);
        player.setVolume(volume, volume);
        sounds.add(player);
        return player;
    }

    public static void setVol(float newVol) {
        for (MediaPlayer player : sounds) {
            player.setVolume(newVol, newVol);
        }
    }
}
