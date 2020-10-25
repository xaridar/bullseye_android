//Dylan coded and created layout
package com.example.bullseye_android.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.music.MusicActivity;

public class AdminSettingsActivity extends AppCompatActivity implements MusicActivity {

    public static final int CHANGE_PASSWORD_REQ_CODE = 20;
    private UserViewModel mUserViewModel;
    private User admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Fetcher.runNewAdminFetcher(mUserViewModel, user -> {
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
        Button changePassword = findViewById(R.id.changePassword);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSettingsActivity.this, ChangeStringActivity.class);
                intent.putExtra("type", "password");
                intent.putExtra("oldPassword", admin.getPassword());
                startActivityForResult(intent, CHANGE_PASSWORD_REQ_CODE);
            }
        });
        manageProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSettingsActivity.this, MoreUsersActivity.class);
                intent.putExtra("MoreUsersContext", 1);
                startActivity(intent);
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
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            mUserViewModel.deleteAll();
//                            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 1000, new Intent(new Intent(getApplicationContext(), HomeActivity.class)), PendingIntent.FLAG_CANCEL_CURRENT);
//                            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, intent);
                            startActivity(new Intent(AdminSettingsActivity.this, HomeActivity.class));
                            finishAffinity();
                            System.exit(0);
                        });
                warning.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == CHANGE_PASSWORD_REQ_CODE){
            if (data != null) {
                String pass = data.getStringExtra("value");
                if (pass != null) {
                    admin.setPassword(pass);
                    mUserViewModel.update(admin);
                }
            }
        }
    }

    @Override
    public int getMusicId() {
        return R.raw.adminsong;
    }
}