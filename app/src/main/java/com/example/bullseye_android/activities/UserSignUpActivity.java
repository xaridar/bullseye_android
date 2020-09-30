// Aakash coded and created layout
package com.example.bullseye_android.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserSerializable;

import java.util.function.Function;

public class UserSignUpActivity extends AppCompatActivity {
    public static final int AVATAR_REQ_CODE = 0;
    Button btn;
    ImageButton avaPicker;
    String avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);
        avatar = "default";
        run();
    };


    public void run(){
        btn = findViewById(R.id.btn);
        avaPicker = findViewById(R.id.ava);

        btn.setOnClickListener((view) -> {
            Intent intent = new Intent(UserSignUpActivity.this, TransitionActivity.class);
            intent.putExtra("sender", "userSignUp");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserSignUpActivity.this, btn, "bigButton");
            startActivity(intent, options.toBundle());
            finish();
        });

        avaPicker.setOnClickListener(view -> {
            Intent intent = new Intent(UserSignUpActivity.this, AvatarChooserActivity.class);
            intent.putExtra("avatar", avatar);
            startActivityForResult(intent, AVATAR_REQ_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AVATAR_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                avatar = data.getStringExtra("avatar");
                avaPicker.setImageResource(getResources().getIdentifier("pfp_" + data.getStringExtra("avatar"), "drawable", "com.example.bullseye_android"));
            } else {
                Log.i(getPackageName(), "Activity failed.");
            }
        }
    }
}