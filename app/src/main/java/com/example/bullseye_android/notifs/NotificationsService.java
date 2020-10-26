package com.example.bullseye_android.notifs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class NotificationsService extends Service {

    private static Long MILLISECS_PER_DAY = 86400000L;
    private static Long MILLISECS_PER_MIN = 60000L;
    Notifications notif;
    public static int eventID = 0;
    private static long delay = MILLISECS_PER_MIN;
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
        service = NotificationsService.this;
        notif = new Notifications(this);
        service.startForeground(101, notif.getNotification());
        sendNotification();
        setAlarm();
    }

    public void setAlarm(){
        Intent alarmIntent = new Intent(this, NotificationReceiver.class);
        pi = PendingIntent.getBroadcast(
                this, eventID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        eventID+=1;
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        i = new Intent(this, NotificationsService.class);
        //PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);
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
}
