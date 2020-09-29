package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bullseye_android.R;

public class UsersSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        run();
    }

    public void run() {
        setContentView(R.layout.activity_users_settings);

        Button changeAvatar = findViewById(R.id.changeProfileButton);
        Button changeName = findViewById(R.id.changeNameButton);
        TextView musicVolume = findViewById(R.id.musicVolumeText);
        SeekBar musicVolumeBar = findViewById(R.id.musicVolumeBar);
        TextView gameVolume = findViewById(R.id.gameVolumeText);
        SeekBar gameVolumeBar = findViewById(R.id.gameVolumeBar);
        Button backToDashboard = findViewById(R.id.backToDashboardButton);

        backToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}