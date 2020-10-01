package com.example.bullseye_android.database;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class UserSerializable implements Serializable {
    @NonNull
    private transient User user;

    public UserSerializable(User user) {
        this.user = user;
    }

    @NonNull
    public User getUser() {
        return user;
    }
}
