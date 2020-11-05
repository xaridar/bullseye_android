package com.example.bullseye_android.database.user;

public class Admin extends User {

    public Admin(String name, String email, String password) {
        super(name, null);
        super.setAdmin(true);
        super.setEmail(email);
        super.setPassword(password);
    }

}