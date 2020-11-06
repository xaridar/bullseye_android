// All notification code Coded by Aakash Sell
package com.example.bullseye_android.notifs;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bullseye_android.R;

import org.jsoup.select.NodeFilter;

public class Notifications extends ContextWrapper{
    Notify notify;


    public Notifications(Context base) {
        super(base);
        notify = new Notify(this);
    }

    public void createNotification(Context context, String title, String text, Class<? extends AppCompatActivity> activity){
        notify.createNotification(context, title, text, activity);
    }
}