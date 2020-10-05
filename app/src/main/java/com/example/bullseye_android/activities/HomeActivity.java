// Aakash coded and created layout
package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.transition.Fade;

import com.example.bullseye_android.R;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    private boolean shouldFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setExitTransition(new Fade());
        setContentView(R.layout.activity_home);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Looper.prepare();
                runOnUiThread(() -> {

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this);
                    startActivity(new Intent(HomeActivity.this, UsersActivity.class), options.toBundle());
                    shouldFinish = true;
                });
            }
        }, 1600);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (shouldFinish) {
            finish();
        }
    }
}