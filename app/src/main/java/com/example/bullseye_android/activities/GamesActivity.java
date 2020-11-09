package com.example.bullseye_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.games.memory.MemoryActivity;
import com.example.bullseye_android.games.sorting.SortingActivity;
import com.example.bullseye_android.games.turn_based.TurnBasedActivity;
import com.google.android.material.button.MaterialButton;

public class GamesActivity extends AppCompatActivity {
    private LiveData<User> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        user = ViewModelProviders.of(this).get(UserViewModel.class).getUser(getSharedPreferences("userID", 0).getLong("id", 0));
        run();
    }

    private void run() {
        TextView nameText = findViewById(R.id.userWelcomeText);
        user.observe(this, user -> nameText.setText(getString(R.string.welcome, user.getName())));
        MaterialButton matchingGameButton = findViewById(R.id.matchingGameButton);
        MaterialButton sortingGameButton = findViewById(R.id.sortingGameButton);
        MaterialButton turnBasedGameButton = findViewById(R.id.turnBasedGameButton);
        MaterialButton backToDashboard = findViewById(R.id.backToDashboard);
        matchingGameButton.setOnClickListener(v -> startActivity(new Intent(GamesActivity.this, MemoryActivity.class)));
        sortingGameButton.setOnClickListener(v -> startActivity(new Intent(GamesActivity.this, SortingActivity.class)));
        turnBasedGameButton.setOnClickListener(v -> startActivity(new Intent(GamesActivity.this, TurnBasedActivity.class)));
        backToDashboard.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}