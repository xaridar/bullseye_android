package com.example.bullseye_android.database;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.function.Function;

public class Fetcher {
    public static void runNewUserFetcher(UserViewModel userViewModel, LifecycleOwner owner, long id, Function<LiveData<User>, Void> userCallback) {
        new UserFetcher(userViewModel, owner, id, userCallback).run();
    }

    public static void runNewAdminFetcher(UserViewModel userViewModel, LifecycleOwner owner, Function<User, Void> adminCallback) {
        new AdminFetcher(userViewModel, owner, adminCallback).run();
    }
}
