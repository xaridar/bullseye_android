//Dylan coded and created layout
package com.example.bullseye_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.user.Fetcher;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.games.memory.MemoryActivity;
import com.example.bullseye_android.games.sorting.SortingActivity;
import com.example.bullseye_android.games.turn_based.TurnBasedActivity;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.music.MusicManager;
import com.google.android.material.button.MaterialButton;

public class UserDashboardActivity extends AppCompatActivity implements MusicActivity {

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

        Button settingsButton = findViewById(R.id.settingsButton);
        Button logOutButton = findViewById(R.id.logOutButton);
        Button surveyButton = findViewById(R.id.surveyButton);
        MaterialButton gamesButton = findViewById(R.id.gamesButton);

        surveyButton.setOnClickListener(v -> startActivity(new Intent(UserDashboardActivity.this, SurveyActivity.class)));
        surveyButton.setOnClickListener(v -> startActivity(new Intent(this, SurveyActivity.class)));
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserDashboardActivity.this, UsersSettingsActivity.class);
            startActivity(intent);
        });
        logOutButton.setOnClickListener(v -> finish());
        gamesButton.setOnClickListener(v -> startActivity(new Intent(this, GamesActivity.class)));
    }

    public void chars(View view) {
        startActivity(new Intent(this, CharacterIntroActivity.class));
    }
}