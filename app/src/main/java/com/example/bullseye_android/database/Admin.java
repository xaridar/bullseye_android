package com.example.roomtut.users;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

public class Admin extends User {




    public Admin(String name, long id, String email, String password) {
        super(name, true, id);
        super.setEmail(email);
        super.setPassword(password);
    }


}