package com.example.bullseye_android.util;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bullseye_android.App;
import com.example.bullseye_android.activities.UsersActivity;

public class NotificationReceiver extends BroadcastReceiver {
    Notifications notif;
    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        sendNotifications();
    }

    public void sendNotifications(){
        notif = new Notifications(ctx);
        notif.createNotification(ctx, "How have you been?", "We haven't seen you in a while. Why don't you hop back on.", UsersActivity.class);
    }
}

