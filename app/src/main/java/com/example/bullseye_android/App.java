// Elliot coded App and Lifecycle Callbacks for music and pausing
package com.example.bullseye_android;

import android.app.Application;

import com.example.bullseye_android.games.GameCallbacks;
import com.example.bullseye_android.music.MusicCallbacks;
import com.example.bullseye_android.notifs.Notifications;

public class App extends Application {
    public Notifications notifications;
    public App() {
        super();

        registerActivityLifecycleCallbacks(new MusicCallbacks());
        registerActivityLifecycleCallbacks(new GameCallbacks());
    }


    @Override
    public void onCreate() {
        super.onCreate();
        notifications = new Notifications(this);



    }
}

