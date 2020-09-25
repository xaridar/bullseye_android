package com.example.bullseye_android.database;

public class Admin extends User {

    public Admin(String name, long id, String email, String password) {
        super(name, id);
        super.setAdmin(true);
        super.setEmail(email);
        super.setPassword(password);
    }

}