package com.example.bullseye_android.util;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bullseye_android.App;

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
        notif.createNotification(ctx, "Test", "Does this work?", null);
    }
}

