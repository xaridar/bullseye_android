package com.example.bullseye_android.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationsService extends Service {
    Notifications notif;
    public NotificationsService() {
        notif = new Notifications(this);
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

    public Intent getIntent(){
        Intent intent = new Intent(this, NotificationsService.class);
        return intent;
    }

    public void background_notifs(){}
}
