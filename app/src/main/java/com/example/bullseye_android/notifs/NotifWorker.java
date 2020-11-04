package com.example.bullseye_android.notifs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.PeriodicWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class NotifWorker extends Worker {

    PeriodicWorkRequest saveRequest =
            new PeriodicWorkRequest.Builder(this.getClass(), 1, TimeUnit.HOURS)
                    // Constraints
                    .build();
    public NotifWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        return Result.success();
    }
}
