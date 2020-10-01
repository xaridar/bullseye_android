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

    private UserViewModel mUserViewModel;
    private LifecycleOwner owner;
    private ExecutorService service;
    private List<Long> ids = new ArrayList<>();

    public static IDGenerator INSTANCE;

    public IDGenerator(UserViewModel userViewModel, LifecycleOwner owner) {
        mUserViewModel = userViewModel;
        this.owner = owner;
        service = Executors.newFixedThreadPool(100);
        mUserViewModel.getUsers().observe(owner, u -> {
            for (User user : u) {
                ids.add(user.getId());
            }
        });
    }

    public static IDGenerator getInstance(UserViewModel userViewModel, LifecycleOwner owner) {
        if (INSTANCE == null) {
            INSTANCE = new IDGenerator(userViewModel, owner);
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
}
