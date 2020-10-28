//Dylan coded and created layout
package com.example.bullseye_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.music.MusicManager;

public class AdminDashboardActivity extends AppCompatActivity implements MusicActivity {

    private User admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserViewModel mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Fetcher.runNewAdminFetcher(mUserViewModel, user -> {
            if (user != null) {
                admin = user;
                run();
            } else {
                startActivity(new Intent(AdminDashboardActivity.this, AdminSignUpActivity.class));
                finish();
            }
            return null;
        });
    }

    public void run() {
        setContentView(R.layout.activity_admin_dashboard);

        MusicManager.getInstance().setVolume(100);
        startMusic();

        Button statsButton = findViewById(R.id.adminStatsButton);
        Button settingsButton = findViewById(R.id.adminSettingsButton);
        Button logOutButton = findViewById(R.id.adminLogOutButton);
        TextView adminWelcome = findViewById(R.id.adminWelcomeText);

        adminWelcome.setText(getString(R.string.welcome, admin.getName()));

        statsButton.setOnClickListener(v -> startActivity(new Intent(AdminDashboardActivity.this, StatsActivity.class)));
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminSettingsActivity.class);
            startActivity(intent);
        });
        logOutButton.setOnClickListener(v -> finish());
    }

    @Override
    public int getMusicId() {
        return R.raw.adminsong;
    }

    @Override
    public boolean startImmediately() {
        return false;
    }
}