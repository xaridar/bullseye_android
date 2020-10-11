package com.example.bullseye_android.games.memory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import com.example.bullseye_android.R;

public class MemoryInstuctionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_instructions);
        ConstraintLayout page = findViewById(R.id.instructions);
        page.setOnClickListener(view -> {
            finish();
        });
    }
}