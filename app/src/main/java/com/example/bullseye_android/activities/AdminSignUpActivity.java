// Elliot coded and created layout
package com.example.bullseye_android.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bullseye_android.R;
import com.example.bullseye_android.util.EmailChecker;
import com.example.bullseye_android.util.ShowPassListener;

public class AdminSignUpActivity extends AppCompatActivity {

    private Button btn;
    private EditText pass;
    private EditText confPass;
    private EditText name;
    private EditText email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        btn = findViewById(R.id.btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        CheckBox togglePass = findViewById(R.id.togglePass);
        CheckBox toggleConfPass = findViewById(R.id.toggleConfPass);
        pass = findViewById(R.id.password);
        confPass = findViewById(R.id.confPassword);

        btn.setOnClickListener((view) -> {
            if (name.getText().toString().equals("")) {
                createDialogue("The name field cannot be empty.");
                return;
            }
            if (email.getText().toString().equals("")) {
                createDialogue("The email field cannot be empty.");
                return;
            }
            if (pass.getText().toString().equals("")) {
                createDialogue("The password field cannot be empty.");
                return;
            }
            if (confPass.getText().toString().equals("")) {
                createDialogue("The confirm password field cannot be empty.");
                return;
            }
            if (!EmailChecker.checkEmail(email.getText().toString())) {
                createDialogue("The provided email is invalid.");
                return;
            }
            if (!pass.getText().toString().equals(confPass.getText().toString())) {
                createDialogue("Please make sure that both passwords match.");
                return;
            }
            Intent intent = new Intent(AdminSignUpActivity.this, TransitionActivity.class);
            intent.putExtra("sender", "adminSignUp");
            startActivity(intent);
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

    private void createDialogue(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle("Incorrect Input")
                .setPositiveButton("OK", null);
        builder.show();
    }
}
