//designed and coded by Dylan
package com.example.bullseye_android.games.memory;

import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.user.Fetcher;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.util.SfxManager;

import java.util.Timer;
import java.util.TimerTask;

public class MemoryInstructionsActivity extends AppCompatActivity implements MusicActivity {

    ImageButton[] cards = new ImageButton[2];
    int[] resIds;
    int[] colors;
    public MediaPlayer cardTone;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Fetcher.runNewUserFetcher(userViewModel, this, getSharedPreferences("userID", 0).getLong("id", 0), u -> {
            u.observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    MemoryInstructionsActivity.this.user = user;
                    u.removeObserver(this);
                    start();
                }
            });
            return null;
        });
    }
    void start() {
        cardTone = SfxManager.createSfx(this, R.raw.card_flip, (float) user.getGameVolume() / User.MAX_VOLUME);
        setContentView(R.layout.activity_memory_instructions);
        cards[0] = findViewById(R.id.imageButton2);
        cards[1] = findViewById(R.id.imageButton);
        colors = new int[]{R.color.memCardColor1, R.color.memCardColor2};
        resIds = new int[]{R.drawable.ic_mem_img_cow, R.drawable.ic_mem_img_snake};
        ConstraintLayout page = findViewById(R.id.instructions);
        page.setOnClickListener(view -> finish());
        cards[0].setOnClickListener(this::flipCard);
        cards[1].setOnClickListener(this::flipCard);
        cards[0].setSoundEffectsEnabled(false);
        cards[1].setSoundEffectsEnabled(false);
    }

    public void flipCard(View view){
        int index = 0;
        cardTone.start();
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

    @Override
    public int getMusicId() {
        return R.raw.memsong;
    }
}