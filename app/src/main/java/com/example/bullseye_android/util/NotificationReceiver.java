package com.example.bullseye_android.util;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bullseye_android.App;

public class NotificationReceiver extends BroadcastReceiver {
    Notifications notif;

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotifications();
    }

    public void sendNotifications(){
        Context appContext = App.getContext();
        notif = new Notifications(appContext);
        notif.createNotification(appContext, "Test", "Does this work?", null);
    }
}

