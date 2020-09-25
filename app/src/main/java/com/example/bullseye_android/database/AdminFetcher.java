package com.example.bullseye_android.database;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.function.Function;

public class AdminFetcher implements Runnable{

    UserViewModel userViewModel;
    LifecycleOwner owner;
    Function<User, Void> function;

    public AdminFetcher(UserViewModel userViewModel, LifecycleOwner owner, Function<User, Void> function) {
        super();
        this.userViewModel = userViewModel;
        this.owner = owner;
        this.function = function;
    }


    @Override
    public void run() {
        function.apply(userViewModel.getAdmin());
    }
}
