// Elliot coded and created layout
package com.example.bullseye_android.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Admin;
import com.example.bullseye_android.database.AdminFetcher;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.IDGenerator;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserFetcher;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.util.EmailChecker;
import com.example.bullseye_android.util.ShowPassListener;

import java.util.function.Function;

public class AdminSignUpActivity extends AppCompatActivity {

    private Button btn;
    private EditText pass;
    private EditText confPass;
    private EditText name;
    private EditText email;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        btn = findViewById(R.id.btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        CheckBox togglePass = findViewById(R.id.togglePass);
        CheckBox toggleConfPass = findViewById(R.id.toggleConfPass);
        pass = findViewById(R.id.password);
        confPass = findViewById(R.id.confPassword);

        btn.setOnClickListener((view) -> {
            Fetcher.runNewAdminFetcher(userViewModel, this, noAdminCallback);
        });

        togglePass.setOnClickListener(new ShowPassListener(pass, togglePass));
        toggleConfPass.setOnClickListener(new ShowPassListener(confPass, toggleConfPass));

        confPass.setOnEditorActionListener((textView, i, event) -> {
            if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || i == EditorInfo.IME_ACTION_DONE) {
                btn.performClick();
                return true;
            }
            return false;
        });
    }

    public Function<User, Void> noAdminCallback = user -> {
        if (user == null) {
            if (name.getText().toString().equals("")) {
                createDialogue("The name field cannot be empty.");
                return null;
            }
            if (email.getText().toString().equals("")) {
                createDialogue("The email field cannot be empty.");
                return null;
            }
            if (pass.getText().toString().equals("")) {
                createDialogue("The password field cannot be empty.");
                return null;
            }
            if (confPass.getText().toString().equals("")) {
                createDialogue("The confirm password field cannot be empty.");
                return null;
            }
            if (!EmailChecker.checkEmail(email.getText().toString())) {
                createDialogue("The provided email is invalid.");
                return null;
            }
            if (!pass.getText().toString().equals(confPass.getText().toString())) {
                createDialogue("Please make sure that both passwords match.");
                return null;
            }
            userViewModel.insert(new Admin(name.getText().toString(), IDGenerator.getInstance(userViewModel, this).getId(), email.getText().toString(), pass.getText().toString()));

        }
        Intent intent = new Intent(AdminSignUpActivity.this, TransitionActivity.class);
        intent.putExtra("sender", "adminSignUp");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AdminSignUpActivity.this, btn, "bigButton");
        startActivity(intent, options.toBundle());
        finish();
        Log.i("HH", "user exists");
        return null;
    };

    private void createDialogue(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle("Incorrect Input")
                .setPositiveButton("OK", null);
        builder.show();
    }
}
