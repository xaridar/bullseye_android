package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bullseye_android.R;

public class UserDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        run();
    }

    public void run() {
        setContentView(R.layout.activity_user_dashboard);

        Button matchingGameButton = findViewById(R.id.matchingGameButton);
        Button sortingGameButton = findViewById(R.id.sortingGameButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        Button logOutButton = findViewById(R.id.logOutButton);

        matchingGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Goes to matching game
            }
        });
        sortingGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Goes to sorting game
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, UsersSettingsActivity.class);
                startActivity(intent);
            }
        });
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, UsersActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}