package com.example.bullseye_android.database;

import java.io.Serializable;

public class UserSerializable implements Serializable {
    private transient User user;

    public UserSerializable(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
