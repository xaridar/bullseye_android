//dylan coded and designed
package com.example.bullseye_android.activities;

import android.os.Bundle;
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
            if(admin.getEmail() != null && admin.getEmail().contentEquals(email.getText())){
                new SendMail(AdminForgotPassword.this).sendMail("bullseyeapp.no.reply@gmail.com","B7nuXx\"3}A", admin.getEmail(), "new password", "new password");
                Toast.makeText(AdminForgotPassword.this, "Email Sent!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AdminForgotPassword.this, "Incorrect Email Entered", Toast.LENGTH_SHORT).show();
            }
        });
    }
}