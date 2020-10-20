package com.example.bullseye_android.music;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {

    public static MusicManager INSTANCE;
    private MediaPlayer player;

    public static MusicManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MusicManager();
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
        player.setOnCompletionListener(mediaPlayer -> {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        });
        return this;
    }

    public void setVolume(float vol) {
        player.setVolume(vol, vol);
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
        return player.isPlaying();
    }
}
