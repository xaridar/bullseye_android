package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;

public class UsersActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;
    private View.OnClickListener avatarListener = new View.OnClickListener() {
        public void onClick(View v) {
            long contentDescription = Long.parseLong(v.getContentDescription().toString());
            SharedPreferences sharedPreferences = getSharedPreferences("userID", 0);
            sharedPreferences.edit().putLong("id", contentDescription).apply();
            Intent myIntent = new Intent(v.getContext(), UserDashboardActivity.class);
            startActivity(myIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        Fetcher.runNewAdminFetcher(mUserViewModel, this, user -> {
            if (user == null) {
                Toast.makeText(this, "No admin account found. Please make one.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UsersActivity.this, AdminSignUpActivity.class));
                finish();
            } else {
                run();
            }
            return null;
        });


    }

    public void run(){

        // hard-coded users for now, for the four users on the users activity
        mUserViewModel.insert(new User("Chuck", 1, "archer"));
        mUserViewModel.insert(new User("Chris", 2, "default"));
        mUserViewModel.insert(new User("Jeffy", 3, "boy"));
        mUserViewModel.insert(new User("Beverly", 4, "girl"));

        ImageButton button = findViewById(R.id.addUser);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UserSignUpActivity.class);
                startActivity(myIntent);
            }
        });



        ImageButton avatar1 = findViewById(R.id.pfp1);
        avatar1.setOnClickListener(avatarListener);

        ImageButton avatar2 = findViewById(R.id.pfp2);
        avatar2.setOnClickListener(avatarListener);

        ImageButton avatar3 = findViewById(R.id.pfp3);
        avatar3.setOnClickListener(avatarListener);

        ImageButton avatar4 = findViewById(R.id.pfp4);
        avatar4.setOnClickListener(avatarListener);

        Button button6 = findViewById(R.id.adminSignIn);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), AdminSignInActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
