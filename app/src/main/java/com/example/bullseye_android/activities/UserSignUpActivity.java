package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.User;

import java.util.function.Function;

public class UserSignUpActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        };


    public void run(){
        btn = findViewById(R.id.btn2);
        btn.setOnClickListener((view) -> {

            startActivity(new Intent(UserSignUpActivity.this, TransitionActivity.class));
            Intent intent = new Intent(UserSignUpActivity.this, UsersActivity.class);
            intent.putExtra("sender", "userSignUp");
            Log.i(getPackageName(), "user exists");
            startActivity(intent);
        });

    }
}