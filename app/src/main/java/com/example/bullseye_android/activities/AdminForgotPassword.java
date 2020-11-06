//dylan coded and designed
package com.example.bullseye_android.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.user.Fetcher;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.mailsender.SendMail;
import com.example.bullseye_android.util.TempPasswordGenerator;

import java.io.IOException;
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
                String newPassword = null;
                try {
                    newPassword = TempPasswordGenerator.getTempPass(8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String body = "<html>Password is " + newPassword + "<br>Sign in using this password and change it in the settings. (Make sure to write it down if you need to!)</html>";
                String finalNewPassword = newPassword;
                Function<Boolean, Void> onFinish = success -> {
                    if(success){
                        admin.setPassword(finalNewPassword);
                        userViewModel.update(admin);
                        sendEmail.setText("Email Sent!");
                        sendEmail.setEnabled(false);
                        Toast.makeText(AdminForgotPassword.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                    }else{
                        sendEmail.setText("Email Failed!");
                    }
                    return null;
                };
                new SendMail().sendMail(onFinish, "bullseyeapp.no.reply@gmail.com", "B7nuXx\"3}A", admin.getEmail(), "Forgot Password", body, AdminForgotPassword.this);

            } else {
                Toast.makeText(AdminForgotPassword.this, "Incorrect Email Entered", Toast.LENGTH_SHORT).show();
            }
        });
    }
}