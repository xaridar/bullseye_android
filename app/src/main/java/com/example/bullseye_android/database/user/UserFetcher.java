package com.example.bullseye_android.database.user;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.function.Function;

public class UserFetcher implements Runnable {

    UserViewModel userViewModel;
    LifecycleOwner owner;
    long id;
    Function<LiveData<User>, Void> function;

    public UserFetcher(UserViewModel userViewModel, LifecycleOwner owner, long id, Function<LiveData<User>, Void> function) {
        super();
        this.userViewModel = userViewModel;
        this.id = id;
        this.owner = owner;
        this.function = function;
    }

    @Override
    public void run() {
        function.apply(userViewModel.getUser(id));
    }
}
