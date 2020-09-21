// Elliot coded and created layout
package com.example.bullseye_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bullseye_android.R;

public class AdminSignUpActivity extends AppCompatActivity {

    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener((view) -> {
            Intent intent = new Intent(AdminSignUpActivity.this, TransitionActivity.class);
            intent.putExtra("sender", "adminSignUp");
            startActivity(intent);
        });
    }

}
