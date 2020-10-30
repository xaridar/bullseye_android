//Dylan coded and created layout
package com.example.bullseye_android.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.music.MusicManager;
import com.example.bullseye_android.util.SfxManager;

import java.util.Timer;
import java.util.TimerTask;

public class UsersSettingsActivity extends AppCompatActivity implements MusicActivity {
    public static final int AVATAR_REQ_CODE = 1;
    public static final int NAME_REQ_CODE = 2;

    User user;
    UserViewModel mUserViewModel;
    MediaPlayer tonePlayer;
    Timer gameVolTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Fetcher.runNewUserFetcher(mUserViewModel, this, getSharedPreferences("userID", 0).getLong("id", 0), user -> {
            if (user == null) {
                finish();
            } else {
                user.observe(UsersSettingsActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(User u) {
                        if (u == null) {
                            finish();
                        }
                        else {
                            UsersSettingsActivity.this.user = u;
                            user.removeObserver(this);
                            run();
                        }
                    }
                });
            }
            return null;
        });
    }

    public void run() {
        tonePlayer = SfxManager.createSfx(this, R.raw.tone, (float) user.getGameVolume() / User.MAX_VOLUME);
        setContentView(R.layout.activity_users_settings);

        Button changeAvatar = findViewById(R.id.changeProfileButton);
        Button changeName = findViewById(R.id.changeNameButton);
        SeekBar musicVolumeBar = findViewById(R.id.musicVolumeBar);
        SeekBar gameVolumeBar = findViewById(R.id.gameVolumeBar);
        Button backToDashboard = findViewById(R.id.backToDashboardButton);

        musicVolumeBar.setProgress(user.getMusicVolume());

        gameVolumeBar.setProgress(user.getGameVolume());

        backToDashboard.setOnClickListener(v -> finish());

        if (getIntent().getStringExtra("game") != null) {
            backToDashboard.setText(getString(R.string.back_to_game, getIntent().getStringExtra("game")));
        }

        changeName.setOnClickListener(view -> {
            Intent intent = new Intent(UsersSettingsActivity.this, ChangeStringActivity.class);
            intent.putExtra("type", "name");
            intent.putExtra("value", user.getName());
            startActivityForResult(intent, NAME_REQ_CODE);
        });

        musicVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                user.setMusicVolume(i);
                float vol = (float) user.getMusicVolume() / User.MAX_VOLUME;
                MusicManager.getInstance().setVolume(vol);
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
                float vol = (float) user.getGameVolume() / User.MAX_VOLUME;
                SfxManager.setVol(vol);
                if (gameVolTimer != null) {
                    gameVolTimer.cancel();
                }
                gameVolTimer = new Timer();
                gameVolTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        tonePlayer.start();
                    }
                }, 500);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        changeAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(UsersSettingsActivity.this, AvatarChooserActivity.class);
            intent.putExtra("avatar", user.getAvatar());
            startActivityForResult(intent, AVATAR_REQ_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AVATAR_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) user.setAvatar(data.getStringExtra("avatar"));
            }
        } else if (requestCode == NAME_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String val = data.getStringExtra("value");
                    if (val != null) user.setName(val);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        mUserViewModel.update(user);
        super.finish();
    }

    @Override
    public int getMusicId() {
        return 0;
    }
}