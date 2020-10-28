// Aakash designed
package com.example.bullseye_android.games.sorting;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bullseye_android.R;
import com.example.bullseye_android.music.MusicActivity;

public class SortingInstructionsActivity extends AppCompatActivity implements MusicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting_instructions);
        ConstraintLayout page = findViewById(R.id.instructions);
        page.setOnClickListener(view -> finish());
    }

    @Override
    public int getMusicId() {
        return R.raw.sortingsong;
    }
}