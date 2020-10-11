package com.example.bullseye_android.games.memory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.bullseye_android.R;

import java.util.Timer;
import java.util.TimerTask;

public class MemoryInstructionsActivity extends AppCompatActivity {

    ImageButton cards[] = new ImageButton[2];
    int resIds[];
    int colors[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_instructions);
        cards[0] = findViewById(R.id.imageButton2);
        cards[1] = findViewById(R.id.imageButton);
        colors = new int[]{R.color.memCardColor1, R.color.memCardColor2};
        resIds = new int[]{R.drawable.mem_img_cow, R.drawable.mem_img_snake};
        ConstraintLayout page = findViewById(R.id.instructions);
        page.setOnClickListener(view -> {
            finish();
        });
        cards[0].setOnClickListener(this::flipCard);
        cards[1].setOnClickListener(this::flipCard);
    }

    public void flipCard(View view){
        int index = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] == view){
                index = i;
                cards[i].setPadding(10,0,10, 0);
                cards[i].setImageResource(resIds[i]);
                cards[i].setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.memCardFront)));
            }
        }
        Timer timer = new Timer();
        int finalIndex = index;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    cards[finalIndex].setImageResource(0);
                    cards[finalIndex].setBackgroundTintList(ColorStateList.valueOf(getColor(colors[finalIndex])));
                });
            }
        }, 600);
    }
}