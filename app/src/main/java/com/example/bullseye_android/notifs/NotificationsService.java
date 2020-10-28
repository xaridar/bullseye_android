// All notification code Coded by Aakash Sell
package com.example.bullseye_android.notifs;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

public class NotificationsService extends Service {

    private static Long MILLISECS_PER_DAY = 86400000L;
    private static Long MILLISECS_PER_MIN = 60000L;
    Notifications notif;
    public static int eventID = 0;
    private static long delay = 10000L;
    //private static long delay = MILLISECS_PER_DAY * 3;
    AlarmManager am;
    Intent i;
    PendingIntent pi;
    static Service service;

    public NotificationsService() {
    }

    public static Service getService() {
        return service;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("service", "service started");
        service = NotificationsService.this;
        notif = new Notifications(this);
        service.startForeground(101, notif.getNotification());
        sendNotification();
        setAlarm();
        dailyNotif();
    }

    public void setAlarm(){
        Intent alarmIntent = new Intent("intent1", Uri.EMPTY, this, NotificationReceiver.class);
        pi = PendingIntent.getBroadcast(
                this, eventID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        eventID+=1;
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
       // i = new Intent(this, NotificationsService.class);
        //PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 0, pi);
    }

    public void stopAlarm(){
        am.cancel(pi);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public void sendNotification(){

        notif.createNotification(this, "background","this is a background notification", null);
    }
    public void dailyNotif(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            Intent alarmIntent = new Intent("intent2", Uri.EMPTY, this, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 35);
            calendar.set(Calendar.SECOND, 1);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }
    }
}
