package com.example.bullseye_android.music;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bullseye_android.R;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.music.MusicManager;

public class MusicCallbacks implements Application.ActivityLifecycleCallbacks {

    private AppCompatActivity activity;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        int id;
        if (activity instanceof MusicActivity) {
            id = ((MusicActivity) activity).getMusicId();
            if (!MusicManager.getInstance().isPlaying() || MusicManager.getInstance().getResId() != id) {
                try {
                    if (id != 0) {
                        MusicManager.newInstance().make(activity.getApplicationContext(), ((MusicActivity) activity).getMusicId()).start();
                    }
                } catch (NullPointerException | IllegalStateException ignored) {
                }
            }
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        this.activity = (AppCompatActivity) activity;
        if (!MusicActivity.class.isAssignableFrom(activity.getClass())) {
            try {
                MusicManager.getInstance().stop();
            } catch (NullPointerException | IllegalStateException ignored) {}
        } else {
            try {
                MusicManager.getInstance().start();
            } catch (NullPointerException | IllegalStateException ignored) {}
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        int id;
        if (activity instanceof MusicActivity) {
            id = ((MusicActivity) activity).getMusicId();
            if (!MusicManager.getInstance().isPlaying() || MusicManager.getInstance().getResId() != id) {
                try {
                    if (id != 0) {
                        MusicManager.newInstance().make(activity.getApplicationContext(), ((MusicActivity) activity).getMusicId()).start();
                    }
                } catch (NullPointerException | IllegalStateException ignored) {
                }
            }
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {}

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if (activity == this.activity) {
            try {
                MusicManager.getInstance().pause();
            } catch (NullPointerException | IllegalStateException ignored) {}
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {}

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {}
}
