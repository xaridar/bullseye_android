// Elliot coded App and Lifecycle Callbacks for music and pausing
package com.example.bullseye_android;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bullseye_android.games.GameCallbacks;
import com.example.bullseye_android.music.MusicCallbacks;

public class App extends Application {

    public App() {
        super();
        registerActivityLifecycleCallbacks(new MusicCallbacks());
        registerActivityLifecycleCallbacks(new GameCallbacks());
    }
}
