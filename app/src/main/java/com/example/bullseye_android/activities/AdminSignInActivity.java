// Aakash coded and created layout
package com.example.bullseye_android.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.user.Fetcher;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.util.ContinueFromEditTextListener;
import com.example.bullseye_android.util.ShowPassListener;

import java.util.function.Function;

public class AdminSignInActivity extends AppCompatActivity {
    CheckBox togglePass;
    EditText pass;
    User admin;
    UserViewModel mUserViewModel;
    Button button;
    EditText name;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_in);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        Function<User, Void> adminCallback = user -> {
            if(user != null){
                admin = user;
                run();
            }
            else{
                Toast.makeText(this,"There is no admin account", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminSignInActivity.this, AdminSignUpActivity.class));
                finish();
            }
            return null;
        };
        Fetcher.runNewAdminFetcher(mUserViewModel, adminCallback);
    }
    public void run(){
        togglePass = findViewById(R.id.togglePass);
        pass = findViewById(R.id.password);
        name = findViewById(R.id.name);
        togglePass.setOnClickListener(new ShowPassListener(pass, togglePass));
        button = findViewById(R.id.btn);
        forgotPassword = findViewById(R.id.textView11);

        pass.setOnEditorActionListener(new ContinueFromEditTextListener(button));

        String text = "Forgot Password";

        SpannableString spannableString = new SpannableString(text);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(getColor(R.color.color1));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(AdminSignInActivity.this, AdminForgotPassword.class);
                startActivity(intent);
            }
            @Override public void updateDrawState(@NonNull TextPaint ds) {ds.setUnderlineText(false);ds.setColor(getColor(R.color.color3));}
        };
        spannableString.setSpan(clickableSpan, 0, text.length(), 0);
        spannableString.setSpan(backgroundColorSpan, 0, text.length(), 0);

        forgotPassword.setText(spannableString);
        forgotPassword.setMovementMethod(LinkMovementMethod.getInstance());
        button.setOnClickListener((view) -> {
            if (name.getText().toString().equals("")) {
                createDialogue("The name field cannot be empty.");
                return;
            }
            if (pass.getText().toString().equals("")) {
                createDialogue("The password field cannot be empty.");
                return;
            }
            if (!name.getText().toString().equals(admin.getName())) {
                createDialogue("Incorrect Username");
                return;
            }
            if (!pass.getText().toString().equals(admin.getPassword())) {
                createDialogue("Incorrect Password");
                return;
            }
            startActivity(new Intent(AdminSignInActivity.this, AdminDashboardActivity.class));
            finish();
        });

    }

    private void createDialogue(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(s)
                .setTitle("Incorrect Input")
                .setPositiveButton("OK", null);
        builder.show();
    }
}