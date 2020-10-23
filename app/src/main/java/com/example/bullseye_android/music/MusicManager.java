package com.example.bullseye_android.music;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {

    public static MusicManager INSTANCE;
    private MediaPlayer player;
    private int resId;
    private static float v;

    private MusicManager() {
        setVolume(v);
    }

    public static MusicManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = newInstance();
        }
        return INSTANCE;
    }

    public static MusicManager newInstance() {
        try {
            INSTANCE.stop();
        } catch(NullPointerException | IllegalStateException ignored) {}
        INSTANCE = new MusicManager();
        return INSTANCE;
    }

    public MusicManager make(Context context, int resId) {
        player = MediaPlayer.create(context, resId);
        player.setVolume(v, v);
        this.resId = resId;
        player.setOnCompletionListener(mediaPlayer -> {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        });
        return this;
    }

    public void setVolume(float vol) {
        v = vol;
        if (player != null) {
            player.setVolume(vol, vol);
        }
    }

    public void start() {
        player.start();
    }

    public void pause() {
        player.pause();
    }

    public void stop() {
        player.stop();
        player.release();
    }

    public boolean isPlaying() {
        if (player == null) {
            return false;
        } else {
            try {
                return player.isPlaying();
            } catch (IllegalStateException exc) {
                return false;
            }
        }
    }

    public int getResId() {
        return resId;
    }
}
