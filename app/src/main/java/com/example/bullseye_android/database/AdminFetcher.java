package com.example.bullseye_android.database;

import java.util.function.Function;

public class AdminFetcher implements Runnable {

    UserViewModel userViewModel;
    Function<User, Void> function;

    public AdminFetcher(UserViewModel userViewModel, Function<User, Void> function) {
        super();
        this.userViewModel = userViewModel;
        this.function = function;
    }


    @Override
    public void run() {
        function.apply(userViewModel.getAdmin());
    }
}
