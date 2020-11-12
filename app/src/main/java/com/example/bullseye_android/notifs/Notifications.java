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
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.bullseye_android.R;

import org.jsoup.select.NodeFilter;

import java.util.concurrent.TimeUnit;

public class Notifications extends ContextWrapper{
    Notify notify;
    NotifWorker notifWorker;


    public Notifications(Context base) {
        super(base);
        notify = new Notify(this);
        createWorker();

    }

    public void createNotification(Context context, String title, String text, Class<? extends AppCompatActivity> activity){
        notify.createNotification(context, title, text, activity);
    }

    public void createNotifications(Context context, String title, String content, Class<? extends AppCompatActivity> activity){
        notify.createNotification(context, title, content, activity);
    }

    public void createWorker(){
        PeriodicWorkRequest notifRequest =
                new PeriodicWorkRequest.Builder(NotifWorker.class, 24, TimeUnit.HOURS)
                        // Constraints
                        .build();
        WorkManager
                .getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork("Send Notification", ExistingPeriodicWorkPolicy.KEEP, notifRequest);
        Log.i("This works", "Work manager is started!! YAY");

    }
}