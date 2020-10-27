//Dylan coded and created layout
package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.games.memory.MemoryActivity;
import com.example.bullseye_android.games.sorting.SortingActivity;
import com.example.bullseye_android.games.turn_based.TurnBasedActivity;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.music.MusicManager;

public class UserDashboardActivity extends AppCompatActivity implements MusicActivity {
    public static final int SETTINGS_REQ = 100;

    LiveData<User> user;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        SharedPreferences sharedPreferences = getSharedPreferences("userID", 0);
        long id = sharedPreferences.getLong("id",0);
        Fetcher.runNewUserFetcher(userViewModel, this, id, user -> {
            if(user == null){
                finish();
            }else{
                this.user = user;
                run();
            }
            return null;

        });
    }

    @Override
    public boolean startImmediately() {
        return false;
    }

    public void run() {
        setContentView(R.layout.activity_user_dashboard);

        TextView welcomeTxt = findViewById(R.id.userWelcomeText);
        ImageView avatarImg = findViewById(R.id.avatar);
        user.observe(this, user -> {
            float vol = (float) user.getMusicVolume() / User.MAX_VOLUME;
            MusicManager.getInstance().setVolume(vol);
            startMusic();
            welcomeTxt.setText(getString(R.string.welcome, user.getName()));
            avatarImg.setImageResource(getResources().getIdentifier("pfp_" + user.getAvatar(), "drawable", "com.example.bullseye_android"));
        });

        Button matchingGameButton = findViewById(R.id.matchingGameButton);
        Button sortingGameButton = findViewById(R.id.sortingGameButton);
        Button turnBasedGameButton = findViewById(R.id.turnBasedGameButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        Button logOutButton = findViewById(R.id.logOutButton);
        Button surveyButton = findViewById(R.id.surveyButton);

        matchingGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboardActivity.this, MemoryActivity.class));
            }
        });
        surveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(UserDashboardActivity.this, Survey.class));
                }

        });
        sortingGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboardActivity.this, SortingActivity.class));
            }
        });
        turnBasedGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboardActivity.this, TurnBasedActivity.class));
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

                finish();
            }

        });
    }
}