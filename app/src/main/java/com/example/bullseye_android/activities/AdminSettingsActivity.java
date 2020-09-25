package com.example.bullseye_android.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.bullseye_android.R;
import com.example.bullseye_android.util.ShowPassListener;

public class AdminSettingsActivity extends AppCompatActivity {

    private Button manageProfiles;
    private Button backToDashboard;
    private Button deleteProgress;
    private CheckBox togglePass;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        manageProfiles = findViewById(R.id.manageProfiles);
        backToDashboard = findViewById(R.id.backToDashboard);
        deleteProgress = findViewById(R.id.deleteProgress);
        togglePass = findViewById(R.id.togglePass);
        password = findViewById(R.id.password);

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
                //deletes progress from all users, has popup warning
                AlertDialog.Builder warning = new AlertDialog.Builder(AdminSettingsActivity.this)
                        .setTitle(R.string.are_you_sure)
                        .setMessage(R.string.warning_message)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete progress from database
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