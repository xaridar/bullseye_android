// Elliot coded and created layout
package com.example.bullseye_android.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Admin;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.IDGenerator;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.util.ContinueFromEditTextListener;
import com.example.bullseye_android.util.EmailChecker;
import com.example.bullseye_android.util.ShowPassListener;

import java.util.function.Function;

public class AdminSignUpActivity extends AppCompatActivity {

    private Button btn;
    private EditText pass;
    private EditText confPass;
    private EditText name;
    private EditText email;
    private boolean clicked;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clicked = false;

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        Function<User, Void> adminCallback = user -> {
            if (user != null) {
                Intent intent = new Intent(AdminSignUpActivity.this, UsersActivity.class);
                intent.putExtra("sender", "adminSignUp");
                Log.i(getPackageName(), "admin exists");
                startActivity(intent);
                finish();
            } else {
                run();
            }
            return null;
        };
        Fetcher.runNewAdminFetcher(userViewModel, this, adminCallback);
    }

    public void run() {
        setContentView(R.layout.activity_admin_sign_up);

        btn = findViewById(R.id.btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        CheckBox togglePass = findViewById(R.id.togglePass);
        CheckBox toggleConfPass = findViewById(R.id.toggleConfPass);
        pass = findViewById(R.id.password);
        confPass = findViewById(R.id.confPassword);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked) {
                    if (name.getText().toString().equals("")) {
                        AdminSignUpActivity.this.createDialogue("The name field cannot be empty.");
                        return;
                    }
                    if (email.getText().toString().equals("")) {
                        AdminSignUpActivity.this.createDialogue("The email field cannot be empty.");
                        return;
                    }
                    if (pass.getText().toString().equals("")) {
                        AdminSignUpActivity.this.createDialogue("The password field cannot be empty.");
                        return;
                    }
                    if (confPass.getText().toString().equals("")) {
                        AdminSignUpActivity.this.createDialogue("The confirm password field cannot be empty.");
                        return;
                    }
                    if (!EmailChecker.checkEmail(email.getText().toString())) {
                        AdminSignUpActivity.this.createDialogue("The provided email is invalid.");
                        return;
                    }
                    if (!pass.getText().toString().equals(confPass.getText().toString())) {
                        AdminSignUpActivity.this.createDialogue("Please make sure that both passwords match.");
                        return;
                    }
                    btn.setEnabled(false);
                    clicked = true;
                    userViewModel.insert(new Admin(name.getText().toString(), IDGenerator.getInstance(userViewModel, AdminSignUpActivity.this).getId(), email.getText().toString(), pass.getText().toString()));
                    Toast.makeText(AdminSignUpActivity.this, "Admin account created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminSignUpActivity.this, TransitionActivity.class);
                    intent.putExtra("sender", "adminSignUp");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AdminSignUpActivity.this, btn, "bigButton");
                    AdminSignUpActivity.this.startActivity(intent, options.toBundle());
                    AdminSignUpActivity.this.finish();
                }
            }
        });

        togglePass.setOnClickListener(new ShowPassListener(pass, togglePass));
        toggleConfPass.setOnClickListener(new ShowPassListener(confPass, toggleConfPass));

        confPass.setOnEditorActionListener(new ContinueFromEditTextListener(btn));
    };

    private void createDialogue(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle("Incorrect Input")
                .setPositiveButton("OK", null);
        builder.show();
    }
}
