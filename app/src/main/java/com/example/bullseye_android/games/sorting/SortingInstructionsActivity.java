package com.example.bullseye_android.games.sorting;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.MotionEvent;

import com.example.bullseye_android.R;
import com.example.bullseye_android.music.MusicActivity;

public class SortingInstructionsActivity extends MusicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting_instructions);
        ConstraintLayout page = findViewById(R.id.instructions);
        page.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }
}