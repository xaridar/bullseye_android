//Dylan coded and created layout
package com.example.bullseye_android.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.util.ShowPassListener;

public class AdminSettingsActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;
    private User admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Fetcher.runNewAdminFetcher(mUserViewModel, this, user -> {
            if (user != null) {
                admin = user;
                run();
            } else {
                startActivity(new Intent(AdminSettingsActivity.this, AdminSignUpActivity.class));
                finish();
            }
            return null;
        });
    }

    public void run() {
        setContentView(R.layout.activity_admin_settings);

        Button manageProfiles = findViewById(R.id.manageProfiles);
        Button backToDashboard = findViewById(R.id.backToDashboard);
        Button deleteProgress = findViewById(R.id.deleteProgress);
        CheckBox togglePass = findViewById(R.id.togglePass);
        EditText password = findViewById(R.id.password);

        password.setText(admin.getPassword());

        manageProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //moves to user delete activity
            }
        });
        backToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        deleteProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder warning = new AlertDialog.Builder(AdminSettingsActivity.this)
                        .setTitle(R.string.are_you_sure)
                        .setMessage(R.string.warning_message)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mUserViewModel.deleteAll();
                                PendingIntent intent = PendingIntent.getActivity(AdminSettingsActivity.this, 1000, getIntent(), PendingIntent.FLAG_CANCEL_CURRENT);
                                System.exit(0);
                            }
                        });
                warning.show();
            }
        });
        togglePass.setOnClickListener(new ShowPassListener(password, togglePass));
    }
}