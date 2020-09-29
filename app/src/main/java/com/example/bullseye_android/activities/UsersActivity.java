package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.bullseye_android.R;

public class UsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        run();

    }

    public void run(){
        ImageButton button = findViewById(R.id.addUser);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UserSignUpActivity.class);
                startActivity(myIntent);
            }
        });
        ImageButton button2 = findViewById(R.id.pfp1);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UserDashboardActivity.class);
                startActivity(myIntent);
            }
        });
        ImageButton button3 = findViewById(R.id.pfp2);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UserDashboardActivity.class);
                startActivity(myIntent);
            }
        });
        ImageButton button4 = findViewById(R.id.pfp3);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UserDashboardActivity.class);
                startActivity(myIntent);
            }
        });
        ImageButton button5 = findViewById(R.id.pfp4);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UserDashboardActivity.class);
                startActivity(myIntent);
            }
        });
        Button button6 = findViewById(R.id.adminSignIn);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), AdminSignInActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
