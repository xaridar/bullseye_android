package com.example.bullseye_android.database;

import java.util.function.Function;

public class IDFromInsertFetcher implements Runnable {

    private UserViewModel userViewModel;
    private Function<Long, Void> callback;
    private User user;

    public IDFromInsertFetcher(UserViewModel userViewModel, Function<Long, Void> callback, User user) {
        this.userViewModel = userViewModel;
        this.callback = callback;
        this.user = user;
    }

    @Override
    public void run() {
        callback.apply(userViewModel.insert(user));
    }
}
