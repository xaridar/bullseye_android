// All notification code Coded by Aakash Sell
package com.example.bullseye_android.notifs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.bullseye_android.activities.UsersActivity;

public class NotificationReceiver extends BroadcastReceiver {
    Notifications notif;
    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        if (intent.getAction().equals("intent1")) {
            this.sendNotifications("Where have you been?", "Please come back we miss you. There is lots of stuff for you to do.", null);
        } else if (intent.getAction().equals("intent2")) {
            this.sendNotifications("Daily Reminder", "Come back and play today, we have missed you!!", null);
        }
    }

    public void sendNotifications(String title, String text, Class <? extends AppCompatActivity> activity){
        notif = new Notifications(ctx);
        notif.createNotification(ctx, title, text, activity);
    }
}
