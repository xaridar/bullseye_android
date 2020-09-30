// Aakash coded and created layout
package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.util.ShowPassListener;

import java.util.function.Function;

public class AdminSignInActivity extends AppCompatActivity {
    CheckBox togglePass;
    EditText pass;
    User admin;
    UserViewModel mUserViewModel;
    Button button;
    EditText name;

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
        Fetcher.runNewAdminFetcher(mUserViewModel, this, adminCallback);
    }
    public void run(){
        togglePass = findViewById(R.id.togglePass);
        pass = findViewById(R.id.password);
        name = findViewById(R.id.name);
        togglePass.setOnClickListener(new ShowPassListener(pass, togglePass));
        button = findViewById(R.id.btn);

        pass.setOnEditorActionListener((textView, i, event) -> {
            if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || i == EditorInfo.IME_ACTION_DONE) {
                button.performClick();
                return true;
            }
            return false;
        });

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
                createDialogue("Incorrect Password");
                return;
            }
            if (!pass.getText().toString().equals(admin.getPassword())) {
                createDialogue("Incorrect Password");
                return;
            }
            startActivity(new Intent(AdminSignInActivity.this, AdminDashboardActivity.class));
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