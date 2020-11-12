//Dylan coded and created layout
package com.example.bullseye_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.survey.Survey;
import com.example.bullseye_android.database.survey.SurveyViewModel;
import com.example.bullseye_android.database.user.Fetcher;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.mailsender.SendMail;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.music.MusicManager;

import java.util.List;
import java.util.function.Function;

public class AdminDashboardActivity extends AppCompatActivity implements MusicActivity {

    private User admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserViewModel mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Fetcher.runNewAdminFetcher(mUserViewModel, user -> {
            if (user != null) {
                admin = user;
                run();
            } else {
                startActivity(new Intent(AdminDashboardActivity.this, AdminSignUpActivity.class));
                finish();
            }
            return null;
        });
    }

    public void run() {
        setContentView(R.layout.activity_admin_dashboard);

        MusicManager.getInstance().setVolume(100);
        startMusic();

        Button statsButton = findViewById(R.id.adminStatsButton);
        Button settingsButton = findViewById(R.id.adminSettingsButton);
        Button surveyButton = findViewById(R.id.adminSurveyButton);
        Button logOutButton = findViewById(R.id.adminLogOutButton);
        TextView adminWelcome = findViewById(R.id.adminWelcomeText);

        adminWelcome.setText(getString(R.string.welcome, admin.getName()));

        statsButton.setOnClickListener(v -> startActivity(new Intent(AdminDashboardActivity.this, StatsActivity.class)));
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminSettingsActivity.class);
            startActivity(intent);
        });
        surveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSurveyEmail();
            }
        });
        logOutButton.setOnClickListener(v -> finish());

    }

    //Coded by Aakash Sell
    public void sendSurveyEmail(){
        String subject = "Survey Results";
        final StringBuilder body = new StringBuilder();
        body.append("<html>");
        body.append("Survey Results. General Feeling About App First. User Responses Second");
        SurveyViewModel viewModel = ViewModelProviders.of(this).get(SurveyViewModel.class);
        LiveData<List<Survey>> data = viewModel.getAll();
        data.observe(this, new Observer<List<Survey>>() {
            @Override
            public void onChanged(List<Survey> surveys) {
                Log.i("Database change", surveys.toString());
                for (Survey survey : surveys) {

                    body.append("<br>");
                    body.append(" ").append(SurveyActivity.getRadioAnswers(survey.getRadioAnswer()));
                    body.append(". ");
                    body.append(" ").append(survey.getInputText());
                    body.append("<br>");
                }
                body.append("</html>");
                new SendMail().sendMail(aBoolean -> null, "bullseyeapp.no.reply@gmail.com", "B7nuXx\"3}A", admin.getEmail(), subject, body.toString(), AdminDashboardActivity.this);
                data.removeObserver(this);
            }

            });

    }



    @Override
    public int getMusicId() {
        return R.raw.adminsong;
    }

    @Override
    public boolean startImmediately() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserViewModel mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Fetcher.runNewAdminFetcher(mUserViewModel, user -> {
            if (user != null) {
                admin = user;
                run();
            } else {
                startActivity(new Intent(AdminDashboardActivity.this, AdminSignUpActivity.class));
                finish();
            }
            return null;
        });
    }
}