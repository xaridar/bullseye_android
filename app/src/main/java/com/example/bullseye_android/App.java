// Elliot coded App and Lifecycle Callbacks for music and pausing
package com.example.bullseye_android;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bullseye_android.games.GameCallbacks;
import com.example.bullseye_android.music.MusicCallbacks;
import com.example.bullseye_android.util.Notifications;
import com.example.bullseye_android.util.NotificationsService;

public class App extends Application {

    private static Context context;

    public App() {
        super();

        registerActivityLifecycleCallbacks(new MusicCallbacks());
        registerActivityLifecycleCallbacks(new GameCallbacks());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setContext(this);
        new Notifications(this);

    }
    public static Context getContext() {
        return context;
    }

    public void setContext(Context mContext) {
        this.context = mContext;
    }


}

