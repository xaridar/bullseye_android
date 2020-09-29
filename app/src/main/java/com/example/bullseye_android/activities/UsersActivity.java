package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.UserViewModel;

public class UsersActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;

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
        ImageButton button = findViewById(R.id.addUser);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UserSignUpActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
        ImageButton button2 = findViewById(R.id.pfp1);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // should go to user dashboard
//                Intent myIntent = new Intent(v.getContext(), UserDashboardActivity.class);
//                startActivityForResult(myIntent, 0);
            }
        });
        ImageButton button3 = findViewById(R.id.pfp2);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // should go to user dashboard
//                Intent myIntent = new Intent(v.getContext(), UserDashboardActivity.class);
//                startActivityForResult(myIntent, 0);
            }
        });
        ImageButton button4 = findViewById(R.id.pfp3);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // should go to user dashboard
//                Intent myIntent = new Intent(v.getContext(), UserDashboardActivity.class);
//                startActivityForResult(myIntent, 0);
            }
        });
        ImageButton button5 = findViewById(R.id.pfp4);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // should go to user dashboard
//                Intent myIntent = new Intent(v.getContext(), UserDashboardActivity.class);
//                startActivityForResult(myIntent, 0);
            }
        });
        Button button6 = findViewById(R.id.adminSignIn);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), AdminSignInActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }
}
