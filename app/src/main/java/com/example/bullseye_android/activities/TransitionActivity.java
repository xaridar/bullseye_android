// Elliot coded and created layout
package com.example.bullseye_android.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.transition.Fade;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bullseye_android.R;

import java.util.Timer;
import java.util.TimerTask;

public class TransitionActivity extends AppCompatActivity {

    boolean shouldFinish;
    private Class<? extends AppCompatActivity> toActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shouldFinish = false;
        super.onCreate(savedInstanceState);
        getWindow().setExitTransition(new Fade());
        setContentView(R.layout.activity_transition);
        Intent intent = getIntent();
        String sendingActivity = intent.getStringExtra("sender");
        if (sendingActivity != null) {
            if (sendingActivity.equals("adminSignUp")) {
                // users activity
            } else if (sendingActivity.equals("userSignUp")) {
                // user dashboard
            }
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Looper.prepare();
                runOnUiThread(() -> {
                    if (toActivity == null) {
                        toActivity = HomeActivity.class;
                    }
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(TransitionActivity.this);
                    startActivity(new Intent(TransitionActivity.this, toActivity), options.toBundle());
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