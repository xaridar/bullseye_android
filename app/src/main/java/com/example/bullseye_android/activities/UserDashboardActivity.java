//Dylan coded and created layout
package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;

public class UserDashboardActivity extends AppCompatActivity {

    User user;
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

    public void run() {
        setContentView(R.layout.activity_user_dashboard);

        TextView welcomeTxt = findViewById(R.id.userWelcomeText);
        welcomeTxt.setText(getString(R.string.welcome, user.getName()));

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