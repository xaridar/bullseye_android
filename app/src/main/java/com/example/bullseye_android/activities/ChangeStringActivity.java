// Elliot coded and designed
package com.example.bullseye_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.bullseye_android.R;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.util.ContinueFromEditTextListener;
import com.example.bullseye_android.util.ShowPassListener;

public class ChangeStringActivity extends AppCompatActivity implements MusicActivity {

    private EditText editText;
    private CheckBox toggle;
    private EditText confEditText;
    private CheckBox confToggle;
    private EditText oldPasswordEditText;
    private CheckBox oldPasswordToggle;
    private Button button;
    private String type;
    private String oldPassword;
    private LinearLayout oldPasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_string);

        if (getIntent() == null) {
            finish();
            return;
        }

        editText = findViewById(R.id.editText);
        toggle = findViewById(R.id.toggle);
        confEditText = findViewById(R.id.confEditText);
        confToggle = findViewById(R.id.confToggle);
        button = findViewById(R.id.button);
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        oldPasswordToggle = findViewById(R.id.oldPasswordToggle);
        oldPasswordLayout = findViewById(R.id.oldPasswordLayout);

        toggle.setOnClickListener(new ShowPassListener(editText, toggle));
        confToggle.setOnClickListener(new ShowPassListener(confEditText, confToggle));
        oldPasswordToggle.setOnClickListener(new ShowPassListener(oldPasswordEditText, oldPasswordToggle));

        type = getIntent().getStringExtra("type");
        String capType = Character.toUpperCase(type.charAt(0)) + type.substring(1);
        getSupportActionBar().setTitle(getString(R.string.change_string, capType));
        if (type.equals("password")) {
            oldPassword = getIntent().getStringExtra("oldPassword");
            editText.setTransformationMethod(new PasswordTransformationMethod());
            toggle.setVisibility(View.VISIBLE);
            confEditText.setVisibility(View.VISIBLE);
            confToggle.setVisibility(View.VISIBLE);
            oldPasswordLayout.setVisibility(View.VISIBLE);
            confEditText.setOnEditorActionListener(new ContinueFromEditTextListener(button));
        } else {
            confEditText.setVisibility(View.INVISIBLE);
            confToggle.setVisibility(View.INVISIBLE);
            toggle.setVisibility(View.GONE);
            oldPasswordLayout.setVisibility(View.GONE);
            if (type.equals("email")) {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            } else if (type.equals("name")) {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            }
            editText.setOnEditorActionListener(new ContinueFromEditTextListener(button));
        }
        editText.setHint(capType);

        String sentVal = getIntent().getStringExtra("value");
        if (sentVal != null) {
            editText.setText(sentVal);
        }

        button.setOnClickListener(view -> {
            if (type.equals("password")) {
                Log.i("HH", oldPassword);
                if (oldPasswordEditText.getText().toString().equals("")) {
                    createDialogue("Please input your existing password to change it.");
                    return;
                }
                if (editText.getText().toString().equals("")) {
                    createDialogue("Please enter a new password.");
                    return;
                }
                if (confEditText.getText().toString().equals("")) {
                    createDialogue("Please confirm the updated password.");
                    return;
                }
                if (!oldPasswordEditText.getText().toString().equals(oldPassword)) {
                    createDialogue("The old password is incorrect.");
                    return;
                }
                if (!editText.getText().toString().equals(confEditText.getText().toString())) {
                    createDialogue("Please make sure that the 'Confirm Password' field matches the new password in the 'Password' field.");
                    return;
                }
            } else {
                if (editText.getText().toString().equals("")) {
                    createDialogue("Please enter a new " + type + ".");
                    return;
                }
            }
            Intent backIntent = new Intent();
            backIntent.putExtra("value", editText.getText().toString());
            setResult(RESULT_OK, backIntent);
            finish();
        });
    }

    private void createDialogue(String s) {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(s)
                .setTitle("Incorrect Input")
                .setPositiveButton("OK", null);
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (type.equals("password")) {
                setResult(RESULT_CANCELED);
                finish();
            } else {
                button.performClick();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (type.equals("password")) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            button.performClick();
        }
    }
}