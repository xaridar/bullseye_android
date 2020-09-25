package com.example.bullseye_android.database;

import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class IDGenerator {

    private AndroidViewModel mWordViewModel;
    private LifecycleOwner owner;
    private ExecutorService service;
    private List<Long> ids = new ArrayList<>();

    public static IDGenerator INSTANCE;

    public IDGenerator(AndroidViewModel androidViewModel, LifecycleOwner owner) {
        mWordViewModel = androidViewModel;
        this.owner = owner;
        service = Executors.newFixedThreadPool(100);
        service.submit(new getIDSAsync());
    }

    public static IDGenerator getInstance(AndroidViewModel androidViewModel, LifecycleOwner owner) {
        if (INSTANCE == null) {
            INSTANCE = new IDGenerator(androidViewModel, owner);
        }
        return INSTANCE;
    }

    public long getId() {
        long id = 0;
        do {
            long randLong = ThreadLocalRandom.current().nextLong(10000000L, 1000000000000L);
            if (!ids.contains(randLong)) {
                id = randLong;
            }
        } while (id == 0);
        return id;
    }

    private class getIDSAsync implements Callable<Void> {

        @Override
        public Void call() throws Exception {
            List<User> users = new ArrayList<>();
            ((UserViewModel) mWordViewModel).getUsers().observe(owner, u -> {
                users.addAll(u);
                for (User user : users) {
                    ids.add(user.getId());
                }
            });
            return null;
        }
    }
}
