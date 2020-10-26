// Coded by Aakash Sell
package com.example.bullseye_android.util;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bullseye_android.App;
import com.example.bullseye_android.R;
import com.example.bullseye_android.activities.UsersActivity;
import com.example.bullseye_android.database.User;

import static android.provider.Settings.System.getString;

public class Notifications extends ContextWrapper {

    private NotificationManager nManager;
    private static final String CHANNEL_ID = "bullseye_channel_id";
    public static final String channel_name = "bullseye_channel";
    public static final String channel_description = "Notification channel for bullseye app";
    boolean channelExists;
    String name;
    String description;
    public NotificationCompat.Builder builder;
    public int notificationId = 101;
    public NotificationsService service;

    public Notifications(Context base) {
        super(base);
        this.createNotificationChannel();
        this.backgroundNotifications();
    }

    public void createNotification(Context context, String title, String text, AppCompatActivity activity){

        if (activity != null) {
            Intent intent = new Intent(this, activity.getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        }


        builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder
            .setSmallIcon(R.drawable.ic_bullseye_logo)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }
    public void createNotificationChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            name = channel_name;
            description = channel_description;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.createNotificationChannel(channel);
        }
    }
    public void backgroundNotifications(){
        Intent intent = new Intent(this, NotificationsService.class);
        startService(intent);
    }

}
