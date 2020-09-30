package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;

public class UsersSettingsActivity extends AppCompatActivity {

    User user;
    UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Fetcher.runNewUserFetcher(mUserViewModel, this, getSharedPreferences("userID", 0).getLong("id", 0), user -> {
            if (user == null) {
                finish();
            } else {
                this.user = user;
                run();
            }
            return null;
        });
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

        musicVolumeBar.setProgress(user.getMusicVolume());

        gameVolumeBar.setProgress(user.getGameVolume());

        backToDashboard.setOnClickListener(v -> {
            mUserViewModel.update(user);
            finish();
        });

        musicVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                user.setMusicVolume(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        gameVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                user.setGameVolume(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}