// All notification code Coded by Aakash Sell
package com.example.bullseye_android.notifs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.bullseye_android.activities.UsersActivity;

public class NotificationReceiver extends BroadcastReceiver {
    Notifications notif;
    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("recive", "service recieved");
        ctx = context;
        if (intent.getAction().equals("intent1")) {
            Log.i("alarm", "alarm");
            this.sendNotifications("Where have you been?", "Please come back we miss you. There is lots of stuff for you to do.", null);
        } else if (intent.getAction().equals("intent2")) {
            Log.i("daily", "daily notifs working");
            this.sendNotifications("Daily Reminder", "Come back and play today, we have missed you!!", null);
        }
        else {
            sendNotifications("working","workin", null);
        }
    }

    public void sendNotifications(String title, String text, Class <? extends AppCompatActivity> activity){
        notif = new Notifications(ctx);
        notif.createNotification(ctx, title, text, activity);
    }
}
