// Aakash coded and created layout
package com.example.bullseye_android.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.user.Fetcher;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.util.ContinueFromEditTextListener;

public class UserSignUpActivity extends AppCompatActivity {
    public static final int AVATAR_REQ_CODE = 0;
    private Button btn;
    private ImageButton avaPicker;
    private String avatar;
    private EditText name;
    private UserViewModel userViewModel;
    boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);
        avatar = "default";

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        run();
    }


    public void run(){
        btn = findViewById(R.id.btn);
        avaPicker = findViewById(R.id.ava);
        name = findViewById(R.id.name);

        clicked = false;

        btn.setOnClickListener((view) -> {
            if (!clicked) {
                if (name.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setMessage("Please enter a name.")
                            .setTitle("Incorrect Input")
                            .setPositiveButton("OK", null);
                    builder.show();
                    return;
                }
                clicked = true;
                Intent intent = new Intent(UserSignUpActivity.this, TransitionActivity.class);
                intent.putExtra("sender", "userSignUp");
                User u = new User(name.getText().toString(), avatar);
                Fetcher.getIDFromInsert(userViewModel, id -> {
                    if (id != -1) {
                        getSharedPreferences("userID", 0).edit().putLong("id", id).apply();
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserSignUpActivity.this, btn, "bigButton");
                        startActivity(intent, options.toBundle());
                        finish();
                    }
                    return null;
                }, u);
            }
        });

        avaPicker.setOnClickListener(view -> {
            Intent intent = new Intent(UserSignUpActivity.this, AvatarChooserActivity.class);
            intent.putExtra("avatar", avatar);
            startActivityForResult(intent, AVATAR_REQ_CODE);
        });

        name.setOnEditorActionListener(new ContinueFromEditTextListener(btn));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AVATAR_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    avatar = data.getStringExtra("avatar");
                    avaPicker.setImageResource(getResources().getIdentifier("pfp_" + data.getStringExtra("avatar"), "drawable", "com.example.bullseye_android"));
                } else {
                    Log.i(getPackageName(), "Activity failed.");
                }
            } else {
                Log.i(getPackageName(), "Activity failed.");
            }
        }
    }
}