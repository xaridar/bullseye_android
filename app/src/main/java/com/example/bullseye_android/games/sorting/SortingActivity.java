// Elliot coded basic game, Aakash changed and polished, Aakash designed
package com.example.bullseye_android.games.sorting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.games.Game;
import com.example.bullseye_android.games.GamePauseFragment;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.util.SfxManager;
import com.example.bullseye_android.util.TimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import nl.dionsegijn.konfetti.KonfettiView;

public class SortingActivity extends AppCompatActivity implements Game, MusicActivity {

    private ConstraintLayout layout;

    private int backCount;
    private Toast toast;


    private RadioGroup speedChoice;
    private String choice;
    private ConstraintLayout start;
    private LinearLayout livesLayout;
    private TextView countdown;
    private TextView timer;
    private ConstraintLayout finishedLayout;
    private TextView highScore;
    private TextView finalTime;
    private ImageButton pauseButton;
    ImageView rightArrow;
    ImageView leftArrow;
    String colorHit;
    GestureDetector gestureDetector;
    Animation animFadeIn;
    Animation animFadeOut;

    private int sent;
    private int correct;
    private int time;
    private int[] millis = new int[1];
    private int[] delay = new int[1];
    private float[] dropSpeed = new float[1];
    private int[] lives = new int[1];
    private Timer constant;
    private Timer timer1;
    private boolean pause;

    private MediaPlayer swipeTone;
    private MediaPlayer correctTone;
    private MediaPlayer incorrectTone;
    private MediaPlayer blackBallTone;

    private User user;
    private UserViewModel userViewModel;
    int gameInt;
    KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
        SharedPreferences prefs = getSharedPreferences("userID", MODE_PRIVATE);
        gestureDetector = new GestureDetector(this, new GestureListener());
        long id = prefs.getLong("id", 0);
        rightArrow = findViewById(R.id.rightArrow);
        leftArrow = findViewById(R.id.leftArrow);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        LiveData<User> mUser = userViewModel.getUser(id);
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        mUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                SortingActivity.this.user = user;
                mUser.removeObserver(this);
                run();
            }
        });
        konfettiView = findViewById(R.id.viewKonfetti);
    }
    public void run() {

        float vol = (float) user.getGameVolume() / User.MAX_VOLUME;

        swipeTone = SfxManager.createSfx(this, R.raw.swipe, vol);
        correctTone = SfxManager.createSfx(this, R.raw.mem_correct, vol);
        incorrectTone = SfxManager.createSfx(this, R.raw.mem_wrong, vol);
        blackBallTone = SfxManager.createSfx(this, R.raw.black_ball, vol);

        rightArrow.startAnimation(animFadeOut);
        leftArrow.startAnimation(animFadeOut);
        backCount = 0;
        toast = Toast.makeText(this, "Press the back button twice at any time to go back to the dashboard.", Toast.LENGTH_SHORT);
        toast.show();

        Button startBtn = findViewById(R.id.btn);
        speedChoice = findViewById(R.id.speedChoice);
        start = findViewById(R.id.start);
        countdown = findViewById(R.id.countdown);
        livesLayout = findViewById(R.id.livesLayout);
        layout = findViewById(R.id.layout);
        timer = findViewById(R.id.time);
        finishedLayout = findViewById(R.id.settingsLayout);
        highScore = findViewById(R.id.highScore);
        Button backBtn = findViewById(R.id.backBtn);
        Button playAgain = findViewById(R.id.playAgain);
        finalTime = findViewById(R.id.finalTime);
        pauseButton = findViewById(R.id.pauseButton2);
        backBtn.setOnClickListener((view) -> finish());

        playAgain.setOnClickListener((view) -> {
            finishedLayout.setVisibility(View.INVISIBLE);
            start.setVisibility(View.VISIBLE);
        });

        start.setVisibility(View.VISIBLE);
        countdown.setVisibility(View.INVISIBLE);
        finishedLayout.setVisibility(View.INVISIBLE);
        highScore.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);


        startBtn.setOnClickListener(view -> {
            RadioButton choice = findViewById(speedChoice.getCheckedRadioButtonId());
            countdown(choice.getText().toString());
        });
    }

    private void countdown(String speed) {

        countdown.setTextSize(250);
        time = 0;
        this.choice = speed;
        if (choice.equals("Slow")) {
            gameInt = User.GAME_SORTING_SLOW;
        } else {
            gameInt = User.GAME_SORTING_FAST;
        }
        setScore();
        highScore.setVisibility(View.VISIBLE);
        start.setVisibility(View.INVISIBLE);
        countdown.setVisibility(View.VISIBLE);


        final int[] time = {3};
        time[0]++;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (time[0] > 1) {
                        time[0] -= 1;
                        countdown.setText(getString(R.string.num, time[0]));
                    } else {
                        cancel();
                        countdown.setTextSize(80);
                        countdown.setText(getString(R.string.start).toUpperCase());
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {

                                runOnUiThread(() -> {
                                    countdown.setVisibility(View.INVISIBLE);
                                    countdown.setText("");
                                });
                                runOnUiThread(SortingActivity.this::startGame);
                            }
                        }, 1000);
                    }
                });
            }
        }, 0, 1000);
    }
    List<ImageButton> views;

    private void startGame() {
        delay[0] = choice.equals("Slow") ? 1000 : 750;
        dropSpeed[0] = choice.equals("Slow") ? 10 : 8;
        lives[0] = 3;
        for (int i = 0; i < lives[0]; i++) {
            ImageView heart = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            heart.setLayoutParams(params);
            heart.setImageResource(R.drawable.heart);
            livesLayout.addView(heart);
        }
        views = new ArrayList<>();
        timer1 = new Timer();
        SpawnTask task = new SpawnTask(this, views);
        timer1.schedule(task, 0);
        views = task.views;
        resetTimer();
        pauseButton.setVisibility(View.VISIBLE);
    }

    private void end() {
        for (ImageButton view : views) {
            layout.removeView(view);
        }
        confetti(konfettiView, this.getApplicationContext());
        views.clear();
        finishedLayout.setVisibility(View.VISIBLE);
        finalTime.setText(getString(R.string.survived_for, timer.getText().toString()));

        user.addGame(gameInt, (float) correct / sent, time, calcPoints((float) correct / sent, time));
        setScore();
    }

    private void setScore() {
        String[] formattedTime = TimeFormatter.formatTime(user.getHighScores()[gameInt]);
        runOnUiThread(() -> highScore.setText(getString(R.string.high_score, formattedTime[0], formattedTime[1])));
    }

    private int calcPoints(float acc, long time) {
        // determine a good points algorithm
        return (int) (10000 * acc / time * (choice.equals("Fast") ? 2 : 1));
    }

    public HashMap<String, Object> checkForCollide(List<ImageButton> views, int lives) {
        final View despawn = findViewById(R.id.despawn);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        float height = displayMetrics.heightPixels;
        String side;
        List<ImageButton> delete = new ArrayList<>();
        for (ImageButton imageButton : views) {
                if (imageButton.getVisibility() == View.VISIBLE && ((ConstraintLayout.LayoutParams) imageButton.getLayoutParams()).topMargin >= height - (((ConstraintLayout.LayoutParams) despawn.getLayoutParams()).bottomMargin + 225)) {
                    side = "bottom";
                    delete.add(imageButton);
                    layout.removeView(imageButton);
                    if (!side.contentEquals(imageButton.getContentDescription())) {
                        incorrectTone.start();
                        lives--;
                    }else{
                        blackBallTone.start();
                        correct ++;
                    }
                    sent++;
                }
                else if(imageButton.getVisibility() == View.INVISIBLE && !colorHit.equals("")) {
                    side = colorHit;
                    delete.add(imageButton);
                    layout.removeView(imageButton);
                    if (!side.contentEquals(imageButton.getContentDescription())) {
                        incorrectTone.start();
                        lives--;
                    }else{
                        correctTone.start();
                        correct ++;
                    }
                    sent++;


              }
            //}
        }
        views.removeAll(delete);
        final HashMap<String, Object> objs = new HashMap<>();
        objs.put("views", views);
        objs.put("lives", lives);
        return objs;
    }

    public void viewInstructions(View view) {
        startActivity(new Intent(this, SortingInstructionsActivity.class));
    }

    @Override
    public void pause(View view) {
        pause = true;
        constant.cancel();
        pauseButton.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.root, GamePauseFragment.newInstance(user)).commit();
    }

    @Override
    public void unpause() {
        pause = false;
        runOnUiThread(() -> pauseButton.setVisibility(View.VISIBLE));
        resetTimer();
    }

    public void resetTimer() {
        constant = new Timer();
        constant.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    millis[0]++;
                    if (millis[0] % delay[0] == 0) {
                        SpawnTask task = new SpawnTask(SortingActivity.this, views);
                        timer1.schedule(task, 0);
                        if (dropSpeed[0] > 3) {
                            dropSpeed[0] -= 0.05;
                        }
                        views = task.views;
                    }
                    if (millis[0] % ((int) dropSpeed[0]) == 0) {
                        DropTask task = new DropTask(views, lives[0]);
                        timer1.schedule(task, 0);
                        views = task.views;
                    }
                    if (millis[0] % 1000 == 0) {
                        time++;
                        String[] formattedTime = TimeFormatter.formatTime(time);
                        timer.setText(getString(R.string.time, formattedTime[0], formattedTime[1]));
                    }
                    HashMap<String, Object> objs = checkForCollide(views, lives[0]);
                    views = (List<ImageButton>) objs.get("views");
                    lives[0] = (int) objs.get("lives");
                    livesLayout.removeAllViews();
                    for (int i = 0; i < lives[0]; i++) {
                        ImageView heart = new ImageView(SortingActivity.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.weight = 1;
                        heart.setLayoutParams(params);
                        heart.setImageResource(R.drawable.heart);
                        livesLayout.addView(heart);
                    }


                    if (lives[0] == 0) {
                        end();
                        lives[0] = -10;
                        constant.cancel();
                    }
                });
            }
        }, 1, 1);
    }

    @Override
    public String getGame() {
        return "sorting";
    }

    public void back(View view) {
        finish();
    }

    class DropTask extends TimerTask {

        public final List<ImageButton> views;
        public int lives;


        public DropTask (List<ImageButton> imageButtons, int lvs) {
            views = imageButtons;
            lives = lvs;
        }

        @Override
        public void run() {
            runOnUiThread(() -> {
                for (ImageButton imageButton : views) {
                    if (imageButton.getVisibility() == View.VISIBLE) {
                        ((ConstraintLayout.LayoutParams) imageButton.getLayoutParams()).topMargin += 10;
                        layout.removeView(imageButton);
                        layout.addView(imageButton);
                    }
                }
            });
        }
    }

    public class SpawnTask extends TimerTask {
        private final Context context;
        public final List<ImageButton> views;
        private final String[] colors = {"left", "right", "bottom"};
        private final Random random;

        public SpawnTask(Context ctx, List<ImageButton> imageButtons) {
            context = ctx;
            views = imageButtons;
            random = new Random();
        }
        @Override
        public void run() {
            runOnUiThread(() -> {
                int size = 200;
                ImageButton imageButton = new ImageButton(context);
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(size, size);
                String color = colors[random.nextInt(colors.length)];
                imageButton.setBackgroundTintList(ColorStateList.valueOf(color.equals("left") ? getColor(R.color.sortingLeft) : color.equals("right") ? getColor(R.color.sortingRight): getColor(android.R.color.black)));
                imageButton.setBackground(ContextCompat.getDrawable(SortingActivity.this, R.drawable.ic_circle));
                imageButton.setId(View.generateViewId());
                layout.addView(imageButton);
                params.endToEnd = ConstraintSet.PARENT_ID;
                params.startToStart = ConstraintSet.PARENT_ID;
                params.topToTop = ConstraintSet.PARENT_ID;
                imageButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                imageButton.setLayoutParams(params);
                imageButton.setContentDescription(color);
                views.add(imageButton);

            });
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!pause){
            return gestureDetector.onTouchEvent(event);
        }else{
            return super.onTouchEvent(event);
        }
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    View view = views.get(0);
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight(view);

                        } else {
                            onSwipeLeft(view);
                        }
                        swipeTone.start();
                        return true;

                    }

                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }

        public void onSwipeRight(View view) {

            colorHit = "right";
            view.setVisibility(View.INVISIBLE);
            rightArrow.startAnimation(animFadeIn);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    rightArrow.setAnimation(animFadeOut);
                }
            }, 1000);
        }

        public void onSwipeLeft(View view) {

            colorHit = "left";
            view.setVisibility(View.INVISIBLE);
            leftArrow.startAnimation(animFadeIn);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    leftArrow.setAnimation(animFadeOut);

                }
            },1000);
        }
    }

    @Override
    public void onBackPressed() {
        backCount++;
        if (backCount >= 2) {
            super.onBackPressed();
            toast.cancel();
            return;
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, "Press back again to go to the dashboard", Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(() -> backCount = 0, 2000);
    }

    @Override
    public void finish() {
        userViewModel.update(user);
        super.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userViewModel.update(user);
    }

    @Override
    public int getMusicId() {
        return R.raw.sortingsong;
    }

    @Override
    public void onResume() {
        super.onResume();
        Fetcher.runNewUserFetcher(userViewModel, this, getSharedPreferences("userID", 0).getLong("id", 0), u -> {
            runOnUiThread(() -> u.observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    SortingActivity.this.user = user;
                    u.removeObserver(this);
                }
            }));
            return null;
        });
    }
}