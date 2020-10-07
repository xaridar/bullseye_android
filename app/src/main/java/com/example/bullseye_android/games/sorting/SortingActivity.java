package com.example.bullseye_android.games.sorting;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.util.TimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SortingActivity extends AppCompatActivity {

    private ConstraintLayout layout;

    final ListenerManager listenerManager = new ListenerManager();
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
    private Button backBtn;
    private Button playAgain;
    private TextView finalTime;

    private SharedPreferences prefs;
    private int sent;
    private int correct;
    private int time;

    private User user;
    private UserViewModel userViewModel;
    int gameInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);

        prefs = getSharedPreferences("userID", MODE_PRIVATE);
        long id = prefs.getLong("id", 0);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        LiveData<User> mUser = userViewModel.getUser(id);
        mUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                SortingActivity.this.user = user;
                mUser.removeObserver(this);
                run();
            }
        });
    }
    public void run() {


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
        finishedLayout = findViewById(R.id.finishedLayout);
        highScore = findViewById(R.id.highScore);
        backBtn = findViewById(R.id.backBtn);
        playAgain = findViewById(R.id.playAgain);
        finalTime = findViewById(R.id.finalTime);

        backBtn.setOnClickListener((view) -> finish());

        playAgain.setOnClickListener((view) -> {
            finishedLayout.setVisibility(View.INVISIBLE);
            start.setVisibility(View.VISIBLE);
        });

        start.setVisibility(View.VISIBLE);
        countdown.setVisibility(View.INVISIBLE);
        finishedLayout.setVisibility(View.INVISIBLE);
        highScore.setVisibility(View.INVISIBLE);

        startBtn.setOnClickListener((View.OnClickListener) view -> {
            RadioButton choice = (RadioButton) findViewById(speedChoice.getCheckedRadioButtonId());
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
                                countdown.setVisibility(View.INVISIBLE);
                                countdown.setText("");
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
        final int[] delay = {choice.equals("Slow") ? 1000 : 750};
        float[] dropSpeed = {choice.equals("Slow") ? 10 : 8};
        final int[] lives = {3};
        for (int i = 0; i < lives[0]; i++) {
            ImageView heart = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            heart.setLayoutParams(params);
            heart.setImageResource(R.drawable.heart);
            livesLayout.addView(heart);
        }
        views = new ArrayList<>();
        Timer timer1 = new Timer();
        SpawnTask task = new SpawnTask(this, views);
        timer1.schedule(task, 0);
        final int[] millis = {0};
        views = task.views;
        Timer constant = new Timer();
        int[] add = {choice.equals("Slow") ? 300 : 250};
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

    private void end() {
        for (ImageButton view : views) {
            layout.removeView(view);
        }
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
            DragListen dragListen = listenerManager.map.get(imageButton);
            if (dragListen != null) {
                if (imageButton.getVisibility() == View.VISIBLE && ((ConstraintLayout.LayoutParams) imageButton.getLayoutParams()).topMargin >= height - (((ConstraintLayout.LayoutParams) despawn.getLayoutParams()).bottomMargin + 225)) {
                    side = "bottom";
                    delete.add(imageButton);
                    layout.removeView(imageButton);
                    if (!side.contentEquals(imageButton.getContentDescription())) {
                        lives--;
                    }
                    sent++;
                } else if(imageButton.getVisibility() == View.INVISIBLE && !dragListen.colorHit.equals("")) {
                    if (listenerManager.map.get(imageButton) != null) {
                        side = dragListen.colorHit;
                        delete.add(imageButton);
                        layout.removeView(imageButton);}
                    else{
                        lives--;
                    }
                    sent++;
                    correct++;

                }
            }
        }
        views.removeAll(delete);
        final HashMap<String, Object> objs = new HashMap<>();
        objs.put("views", views);
        objs.put("lives", lives);
        return objs;
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
        private final String[] colors = {"pink", "brown", "black"};
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
                imageButton.setBackgroundTintList(ColorStateList.valueOf(color.equals("pink") ? getColor(R.color.color3) : color.equals("brown") ? getColor(R.color.color5): getColor(android.R.color.black)));
                imageButton.setBackground(ContextCompat.getDrawable(SortingActivity.this, R.drawable.ic_circle));
                imageButton.setId(View.generateViewId());
                layout.addView(imageButton);
                params.endToEnd = ConstraintSet.PARENT_ID;
                params.startToStart = ConstraintSet.PARENT_ID;
                params.topToTop = ConstraintSet.PARENT_ID;
                imageButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                imageButton.setLayoutParams(params);
                imageButton.setContentDescription(color.equals("pink") ? "right" : color.equals("brown") ? "left" : "bottom");
                listenerManager.addListener(imageButton);
                views.add(imageButton);
            });
        }

    }

    class ListenerManager {
        public Map<ImageButton, DragListen> map;
        public ListenerManager() {
            map = new HashMap<>();
        }

        public void addListener(ImageButton button) {
            DragListen listener = new DragListen();
            map.put(button, listener);
            button.setOnTouchListener(listener);
        }
    }

    class DragListen implements View.OnTouchListener {

        public String colorHit;

        public DragListen () {
            colorHit = "";
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.performClick();
            ClipData data = ClipData.newPlainText("", "");
            //View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            //view.startDragAndDrop(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            layout.setOnDragListener((view1, dragEvent) -> {
                if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
                    if (inViewInBounds(findViewById(R.id.bucket_left), (int) dragEvent.getX(), (int) dragEvent.getY())) {
                        colorHit = "left";
                        //} else if (inViewInBounds(findViewById(R.id.bucket_right), (int) dragEvent.getX(), (int) dragEvent.getY())) {
                        colorHit = "right";
                    } else {
                        view.setVisibility(View.VISIBLE);
                        colorHit = "bottom";
                    }
                }
                return true;
            });
            return false;
        }
    }

    // thanks to AMD on stackoverflow for the solution https://stackoverflow.com/questions/17931816/how-to-tell-if-an-x-and-y-coordinate-are-inside-my-button
    private boolean inViewInBounds(View view, int x, int y) {
        Rect outRect = new Rect();
        int[] location = new int[2];
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
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
}