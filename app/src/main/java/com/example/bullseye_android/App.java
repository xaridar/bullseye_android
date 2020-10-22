package com.example.bullseye_android;

import android.app.Application;
import android.app.Notification;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bullseye_android.games.GameCallbacks;
import com.example.bullseye_android.music.MusicCallbacks;
import com.example.bullseye_android.util.Notifications;

public class App extends Application {

    public App() {
        super();
        registerActivityLifecycleCallbacks(new MusicCallbacks());
        registerActivityLifecycleCallbacks(new GameCallbacks());
    }
}
