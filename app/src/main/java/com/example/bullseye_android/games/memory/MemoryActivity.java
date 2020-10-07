package com.example.bullseye_android.games.memory;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.util.TimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MemoryActivity extends AppCompatActivity {

    private ImageButton[] buttons;
    private MemoryCard[][] cards;
    private LinearLayout board;
    private LinearLayout[] columns;
    private ConstraintLayout diff;
    private Button playBtn;
    private RadioGroup diffChoice;
    private ConstraintLayout finishedLayout;
    private TextView timeTxt;
    private TextView finalTime;
    private TextView pairTxt;
    private TextView highScore;
    private int time;
    private Timer timer;
    private Random random = new Random();
    private int cardsUp = 0;
    private int pairs;
    private String difficulty;
    private SharedPreferences prefs;
    private User user;
    private UserViewModel userViewModel;
    private int diffInt;
    //    public MediaPlayer tonePlayer;
    private ArrayList<MemoryCard> shownCards = new ArrayList<>();
    Toast toast;
    int tries;
    private View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (cardsUp < 2) {
                for (int i = 0; i < buttons.length; i++) {
                    if (view == buttons[i]) {
                        MemoryCard card = cards[(i - (i % cards[0].length)) / cards[0].length][i % cards[0].length];
                        if (card.isFaceDown()) {
                            shownCards.add(card);
                            cardsUp++;
                            card.setFaceDown(false);
                            break;
                        }
                    }
                }
                showBoard();
            }

        }
    };
    private int  backCount;

    private void checkForWin() {
        for (MemoryCard[] card : cards) {
            for (MemoryCard memoryCard : card) {
                if (memoryCard != null) {
                    return;
                }
            }
        }
        board.setVisibility(View.INVISIBLE);
        finishedLayout.setVisibility(View.VISIBLE);
        timeTxt.setVisibility(View.INVISIBLE);
        pairTxt.setVisibility(View.INVISIBLE);
        finalTime.setText(getString(R.string.final_time, timeTxt.getText()));
        timer.cancel();


        user.addGame(diffInt, (float) pairs / tries, time, calcPoints((float) pairs / tries, time));

        setScore();

        findViewById(R.id.playAgain).setOnClickListener(view -> {
            finishedLayout.setVisibility(View.INVISIBLE);
            diff.setVisibility(View.VISIBLE);
            highScore.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);


        prefs = getSharedPreferences("userID", MODE_PRIVATE);
        long id = (prefs.getLong("id", 0));

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        LiveData<User> mUser = userViewModel.getUser(id);
        mUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                MemoryActivity.this.user = user;
                mUser.removeObserver(this);
                run();
            }
        });
    }

    private void run() {
        backCount = 0;

//        tonePlayer = MediaPlayer.create(MemoryActivity.this, R.raw.tone);

        toast = Toast.makeText(this, "Press the back button twice at any time to go back to the dashboard.", Toast.LENGTH_SHORT);
        toast.show();


        board = findViewById(R.id.board);
        finishedLayout = findViewById(R.id.finishedLayout);
        timeTxt = findViewById(R.id.time);
        finalTime = findViewById(R.id.finalTime);
        finishedLayout.setVisibility(View.INVISIBLE);
        pairTxt= findViewById(R.id.pairs);
        highScore = findViewById(R.id.highScore);
        highScore.setVisibility(View.INVISIBLE);
        diff = findViewById(R.id.settingLayout);
        diff.setVisibility(View.VISIBLE);
        playBtn = findViewById(R.id.playBtn);
        diffChoice = findViewById(R.id.diffButtons);

        playBtn.setOnClickListener(view -> {
            difficulty = ((RadioButton) findViewById(diffChoice.getCheckedRadioButtonId())).getText() + "";
            switch (difficulty){
                case "Easy":
                    diffInt = User.GAME_MEMORY_EASY;
                    start(4, 3);
                    break;
                case "Medium":
                    diffInt = User.GAME_MEMORY_NORMAL;
                    start(5, 4);
                    break;
                case "Hard":
                    diffInt = User.GAME_MEMORY_HARD;
                    start(6, 5);
                    break;
            }
            diff.setVisibility(View.INVISIBLE);
        });
        findViewById(R.id.backBtn).setOnClickListener(view -> finish());
    }

    private void setScore() {
        String[] formattedTime = TimeFormatter.formatTime(user.getHighScores()[diffInt]);
        runOnUiThread(() -> highScore.setText(getString(R.string.high_score, formattedTime[0], formattedTime[1])));
    }

    public void start(int y, int x) {
        setScore();
        tries = 0;
        pairs = 0;
        highScore.setVisibility(View.VISIBLE);
        timeTxt.setText(getString(R.string.time, "00", "00"));
        timeTxt.setVisibility(View.VISIBLE);
        pairTxt.setText(getString(R.string.points, pairs));
        pairTxt.setVisibility(View.VISIBLE);
        time = 0;
        cards = new MemoryCard[y][x];
        buttons = new ImageButton[cards.length * cards[0].length];
        columns = new LinearLayout[cards[0].length];
        board.setVisibility(View.VISIBLE);
        board.removeAllViews();
        for (int i = 0; i < columns.length; i++) {
            LinearLayout column = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            params.weight = 1;
            column.setLayoutParams(params);
            column.setOrientation(LinearLayout.VERTICAL);
            column.setBackgroundColor(getColor(android.R.color.transparent));
            board.addView(column);
            columns[i] = column;
        }
        setBoard();
        for(int i = 0; i < buttons.length; i++) {
            ImageButton btn = new ImageButton(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            switch (difficulty) {
                case "Easy":
                    btn.setBackground(ContextCompat.getDrawable(this, R.drawable.corners_easy));
                    if (i % 2 == 0) {
                        btn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color3)));
                    } else {
                        btn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color5)));
                    }
                    params.setMargins(24, 20, 24, 20);
                    break;
                case "Medium":
                    btn.setBackground(ContextCompat.getDrawable(this, R.drawable.corners_normal));
                    if ((i - (i % x)) % 8 == 0) {
                        if (i % 2 == 0) {
                            btn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color3)));
                        } else {
                            btn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color5)));
                        }
                    } else {
                        if (i % 2 == 1) {
                            btn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color3)));
                        } else {
                            btn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color5)));
                        }
                    }
                    params.setMargins(5, 10, 5, 10);
                    break;
                case "Hard":
                    btn.setBackground(ContextCompat.getDrawable(this, R.drawable.corners_hard));
                    if (i % 2 == 0) {
                        btn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color3)));
                    } else {
                        btn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color5)));
                    }
                    params.setMargins(0, 10, 0, 10);
                    break;
            }
            params.weight = 1;
            btn.setLayoutParams(params);
            btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            columns[i % columns.length].addView(btn);
            buttons[i] = btn;
            buttons[i].setOnClickListener(cardListener);
        }
        showBoard();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time++;
                runOnUiThread(() -> {
                    String[] formattedTime1 = TimeFormatter.formatTime(time);
                    timeTxt.setText(getString(R.string.time, formattedTime1[0], formattedTime1[1]));
                });
            }
        }, 1000, 1000);
    }



    private void showBoard() {
        for (int i = 0; i < buttons.length; i++) {
            MemoryCard card = cards[(i - (i % cards[0].length)) / cards[0].length][(i % cards[0].length)];
            if (card != null) {
                // image res for card back/front
                if (card.isFaceDown()) {
                    buttons[i].setImageResource(0);
                } else {
                    String img = "drawable/mem_img_" + card.getType().toLowerCase();
                    int res = getResources().getIdentifier(img, null, "com.example.bullseye_android");
                    buttons[i].setImageResource(res);
                }
            } else {
                buttons[i].setImageResource(0);
                buttons[i].setBackgroundColor(0);
                buttons[i].setOnClickListener(null);
            }
        }
        if (cardsUp >= 2) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        if (shownCards.get(0).getType().equals(shownCards.get(1).getType())) {
//                            tonePlayer.start();
                            for (int i = 0; i < shownCards.size(); i++) {
                                for (int x = 0; x < cards.length; x++) {
                                    for (int y = 0; y < cards[x].length; y++) {
                                        if (cards[x][y] == shownCards.get(i)) {
                                            cards[x][y] = null;
                                        }
                                    }
                                }
                            }
                            pairs++;
                            pairTxt.setText(getString(R.string.points, pairs));
                            checkForWin();
                        } else {
                            for (int i = 0; i < shownCards.size(); i++) {
                                for (MemoryCard[] card : cards) {
                                    for (MemoryCard memoryCard : card) {
                                        if (memoryCard == shownCards.get(i)) {
                                            memoryCard.setFaceDown(true);
                                        }
                                    }
                                }
                            }
                        }
                        tries++;
                        cardsUp = 0;
                        shownCards.clear();
                        showBoard();
                    });
                }
            }, 600);
        }
    }

    public void setBoard(){
        List<MemoryCard> c = new ArrayList<>();
        List<String> types = new ArrayList<>();
        for(int i = 0; i < ((cards.length * cards[0].length) / 2); i++){
            boolean novel = false;
            String type = null;
            while(!novel){
                int randNum = random.nextInt(MemoryCard.getTypes().length);
                type = MemoryCard.getTypes()[randNum];
                novel = true;
                for(String t : types){
                    if (type.equals(t)) {
                        novel = false;
                        break;
                    }
                }
            }
            types.add(type);
        }
        int loopNum = types.size();
        for(int i = 0; i < loopNum; i++){
            int num = random.nextInt(types.size());
            for(int n = 0; n < 2; n++){
                c.add(new MemoryCard(types.get(num)));
            }
            types.remove(num);
        }
        for(int x = 0; x < cards.length; x++){
            for(int y = 0; y < cards[x].length; y++){
                int num = random.nextInt(c.size());
                cards[x][y] = c.get(num);
                c.remove(num);
            }
        }
    }

    @Override
    public void onBackPressed() {
        backCount++;
        if (backCount >= 2) {
            super.onBackPressed();
            return;
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, "Press back again to go to the dashboard", Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(() -> backCount = 0, 2000);
    }

    private int calcPoints(float acc, int time) {
        // determine a good points algorithm
        return (int) (1000 * acc / time * (cards.length * cards[0].length));
    }

    @Override
    public void finish() {
        userViewModel.update(user);
        super.finish();
    }
}