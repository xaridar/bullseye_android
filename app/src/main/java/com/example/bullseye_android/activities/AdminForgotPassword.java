//dylan coded and designed
package com.example.bullseye_android.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.mailsender.SendMail;
import com.example.bullseye_android.util.ContinueFromEditTextListener;
import com.example.bullseye_android.util.TempPasswordGenerator;

import java.util.function.Function;

public class AdminForgotPassword extends AppCompatActivity {

    User admin;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Fetcher.runNewAdminFetcher(userViewModel, user -> {
            if(user != null){
                admin = user;
                run();
            }
            return null;
        });

    }

    public void run() {
        setContentView(R.layout.activity_admin_forgot_password);

        Button sendEmail = findViewById(R.id.sendEmail);
        EditText email = findViewById(R.id.forgotPasswordEmail);

        sendEmail.setOnClickListener(v -> {
            if (admin.getEmail() != null && admin.getEmail().contentEquals(email.getText())) {
                String newPassword = TempPasswordGenerator.getEncodedRandom(8);
                String body = "<html>Password is " + newPassword + "<br>Sign in using this password and change it in the settings. (Make sure to write it down if you need to!)</html>";
                Function<Boolean, Void> onFinish = success -> {
                    if(success){
                        admin.setPassword(newPassword);
                        userViewModel.update(admin);
                    }
                    return null;
                };
                new SendMail(AdminForgotPassword.this).sendMail(onFinish, "bullseyeapp.no.reply@gmail.com", "B7nuXx\"3}A", admin.getEmail(), "Forgot Password", body);
                Toast.makeText(AdminForgotPassword.this, "Email Sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdminForgotPassword.this, "Incorrect Email Entered", Toast.LENGTH_SHORT).show();
            }
        });
    }
}