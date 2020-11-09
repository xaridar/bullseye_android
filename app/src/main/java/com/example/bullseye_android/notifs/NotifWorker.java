package com.example.bullseye_android.notifs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bullseye_android.activities.UsersActivity;

import java.util.concurrent.TimeUnit;

public class NotifWorker extends Worker {

    Notify notification;

    public NotifWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

    }

    @Override
    public Result doWork() {
        notification = new Notify(getApplicationContext());
        notification.createNotification(getApplicationContext(), "Where have you been??", "We have missed you. Come play some games with bullseye!!", UsersActivity.class);
        return Result.success();
    }
}
