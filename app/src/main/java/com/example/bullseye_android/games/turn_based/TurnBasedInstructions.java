package com.example.bullseye_android.games.turn_based;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bullseye_android.R;
import com.example.bullseye_android.music.MusicActivity;

public class TurnBasedInstructions extends AppCompatActivity implements MusicActivity {

    ConstraintLayout instructions1;
    ConstraintLayout instructions2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_based_instructions);

        instructions1 = findViewById(R.id.instructions1);
        instructions2 = findViewById(R.id.instructions2);

        instructions1.setVisibility(View.VISIBLE);
        instructions2.setVisibility(View.INVISIBLE);

        instructions1.setOnClickListener(view -> {
            instructions1.setVisibility(View.INVISIBLE);
            instructions2.setVisibility(View.VISIBLE);
        });

        instructions2.setOnClickListener(view -> finish());
    }

    @Override
    public int getMusicId() {
        return R.raw.strategysong;
    }
}