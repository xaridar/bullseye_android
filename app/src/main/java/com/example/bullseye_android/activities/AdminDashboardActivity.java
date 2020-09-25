package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bullseye_android.R;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button statsButton;
    private Button settingsButton;
    private Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        statsButton = findViewById(R.id.adminStatsButton);
        settingsButton = findViewById(R.id.adminSettingsButton);
        logOutButton = findViewById(R.id.adminLogOutButton);

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //move to stats activity
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, AdminSettingsActivity.class);
                startActivity(intent);
            }
        });
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, UsersActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}