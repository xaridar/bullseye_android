// Stats activity coding was done by Elliot, Aakash designed
package com.example.bullseye_android.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.util.ValueIterator;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.user.Fetcher;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.util.NavAdapter;
import com.example.bullseye_android.util.PDF;
import com.example.bullseye_android.util.TimeFormatter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.itextpdf.text.Element;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;

public class  StatsActivity extends AppCompatActivity implements MusicActivity {

    public static final int MEMORY = 0;
    public static final int SORTING = 1;
    public static final int STRATEGY = 2;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private MutableLiveData<User> currentUser;
    private MutableLiveData<Integer> tab;
    private TextView memory;
    private TextView sorting;
    private TextView strategy;
    private List<TextView> bottomNav = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        UserViewModel mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        currentUser = new MutableLiveData<>();

        Function<LiveData<User>, Void> callback = user -> {
            user.observe(this, new Observer<User>() {
                @Override
                public void onChanged(User u) {
                    currentUser.setValue(u);
                    user.removeObserver(this);
                    run();
                }
            });
            return null;
        };

        Fetcher.runNewUserFetcher(mUserViewModel, this, getSharedPreferences("userID", 0).getLong("id", 0), callback);
    }

    private void run() {

        tab = new MutableLiveData<>();
        RecyclerView rv = findViewById(R.id.rv);
        toolbar = findViewById(R.id.toolbar);
        Toolbar navtoolbar = findViewById(R.id.navtoolbar);
        drawerLayout = findViewById(R.id.drawer);
        sorting = findViewById(R.id.sorting_bottom);
        memory = findViewById(R.id.memory_bottom);
        strategy = findViewById(R.id.strategy_bottom);
        ExtendedFloatingActionButton exportFab = findViewById(R.id.exportFab);

        bottomNav.add(sorting);
        bottomNav.add(memory);
        bottomNav.add(strategy);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ActionBarDrawerToggle navActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, navtoolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(navActionBarDrawerToggle);
        navActionBarDrawerToggle.syncState();

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getColor(android.R.color.white));
        navActionBarDrawerToggle.getDrawerArrowDrawable().setColor(getColor(android.R.color.white));


        final NavAdapter adapter = new NavAdapter(this);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });

        currentUser.observe(this, user -> {
            if (user != null) {
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle(getString(R.string.stats_header, user.getName()));
                adapter.setFirst(user);
            }
            tab.setValue(MEMORY);
        });

        tab.observe(this, integer -> {
            TextView tv = null;
            if (integer == MEMORY) {
                tv = memory;
                if (currentUser.getValue() != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, MemoryStatsFragment.newInstance(currentUser.getValue())).commit();
                }
            } else if (integer == SORTING) {
                tv = sorting;
                if (currentUser.getValue() != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, SortingStatsFragment.newInstance(currentUser.getValue())).commit();
                }
            } else if (integer == STRATEGY) {
                tv = strategy;
                if (currentUser.getValue() != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, StrategyStatsFragment.newInstance(currentUser.getValue())).commit();
                }
            }
            for (TextView textView : bottomNav) {
                if (textView == tv) {
                    textView.setBackgroundColor(getColor(R.color.selectedBg));
                    textView.setTextColor(getColor(R.color.selectedText));
                } else {
                    textView.setBackgroundColor(getColor(android.R.color.transparent));
                    textView.setTextColor(getColor(R.color.color1));
                }
            }
        });

        memory.setOnClickListener(view -> tab.setValue(MEMORY));

        sorting.setOnClickListener(view -> tab.setValue(SORTING));

        strategy.setOnClickListener(view -> tab.setValue(STRATEGY));

        exportFab.setOnClickListener(view -> {
            User user = currentUser.getValue();
            if (user != null) {
                // pdf
                export(user);
            }
        });
    }

    public void changeFirst(User current) {
        this.currentUser.setValue(current);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public int getMusicId() {
        return R.raw.adminsong;
    }

    public void export(User currentUser){
        int i;
        List<String> headers;
        List<String> headers2;
        List<String[]> lines;
        List<String[]> lines2;



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        Calendar calendar = Calendar.getInstance();

        PDF pdf = new PDF(this);
        pdf.openDocument(createName(calendar, currentUser.getName()), 3);
        pdf.addMetaData("Bullseye Stats", currentUser.getName(), "auto-generated by Bullseye");
        String currentTime = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.YEAR);
        pdf.addTitles(currentUser.getName(), "Bullseye Stats - " + currentTime);
        pdf.addHeader("Matching Game", Element.ALIGN_CENTER);
        pdf.addParagraph();
        pdf.addSubheader("Easy", Element.ALIGN_CENTER);
        pdf.addChild("Total Points: " + currentUser.getFocusPoints()[User.GAME_MEMORY_EASY], Element.ALIGN_CENTER);
        pdf.addChild("Games Played: " + currentUser.getGamesPlayed()[User.GAME_MEMORY_EASY], Element.ALIGN_CENTER);
        pdf.addChild("Play Time: " + TimeFormatter.autoFormatTime(currentUser.getPlayTime()[User.GAME_MEMORY_EASY]), Element.ALIGN_CENTER);
        pdf.addChild("High Score: " + TimeFormatter.autoFormatTime(currentUser.getHighScores()[User.GAME_MEMORY_EASY]), Element.ALIGN_CENTER);
        pdf.addChild("Accuracy: " + currentUser.getAccuracy()[User.GAME_MEMORY_EASY], Element.ALIGN_CENTER);
        pdf.addParagraph();
        headers = new ArrayList<>();
        lines = new ArrayList<>();
        headers2 = new ArrayList<>();
        lines2 = new ArrayList<>();
        i = 0;
        for (Number[] game : currentUser.getLastGames()[User.GAME_MEMORY_EASY]) {
            if ((int) ((i + 1) / 3) == 0) {
                headers.add("Game " + (currentUser.getLastGames()[User.GAME_MEMORY_EASY].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.POINTS];
                ls[1] = "Accuracy: " + game[User.ACC];
                ls[2] = "Time: " + game[User.TIME];
                lines.add(ls);
            } else {
                headers2.add("Game" + (currentUser.getLastGames()[User.GAME_MEMORY_EASY].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.POINTS];
                ls[1] = "Accuracy: " + game[User.ACC];
                ls[2] = "Time: " + game[User.TIME];
                lines2.add(ls);
            }
            i++;
        }
        pdf.createTable(headers, lines, Math.min(3, lines.size()));
        if (!lines2.isEmpty()) pdf.createTable(headers2, lines2, Math.min(2, lines2.size()));
        pdf.addSubheader("Normal", Element.ALIGN_CENTER);
        pdf.addChild("Total Points: " + currentUser.getFocusPoints()[User.GAME_MEMORY_NORMAL], Element.ALIGN_CENTER);
        pdf.addChild("Games Played: " + currentUser.getGamesPlayed()[User.GAME_MEMORY_NORMAL], Element.ALIGN_CENTER);
        pdf.addChild("Play Time: " + TimeFormatter.autoFormatTime(currentUser.getPlayTime()[User.GAME_MEMORY_NORMAL]), Element.ALIGN_CENTER);
        pdf.addChild("High Score: " + TimeFormatter.autoFormatTime(currentUser.getHighScores()[User.GAME_MEMORY_NORMAL]), Element.ALIGN_CENTER);
        pdf.addChild("Accuracy: " + currentUser.getAccuracy()[User.GAME_MEMORY_NORMAL], Element.ALIGN_CENTER);
        pdf.addParagraph();
        headers = new ArrayList<>();
        lines = new ArrayList<>();
        headers2 = new ArrayList<>();
        lines2 = new ArrayList<>();
        i = 0;
        for (Number[] game : currentUser.getLastGames()[User.GAME_MEMORY_NORMAL]) {
            if ((int) ((i + 1) / 3) == 0) {
                headers.add("Game " + (currentUser.getLastGames()[User.GAME_MEMORY_NORMAL].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.POINTS];
                ls[1] = "Accuracy: " + game[User.ACC];
                ls[2] = "Time: " + game[User.TIME];
                lines.add(ls);
            } else {
                headers2.add("Game" + (currentUser.getLastGames()[User.GAME_MEMORY_NORMAL].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.POINTS];
                ls[1] = "Accuracy: " + game[User.ACC];
                ls[2] = "Time: " + game[User.TIME];
                lines2.add(ls);
            }
            i++;
        }
        pdf.createTable(headers, lines, Math.min(3, lines.size()));
        if (!lines2.isEmpty()) pdf.createTable(headers2, lines2, Math.min(2, lines2.size()));
        pdf.addSubheader("Hard", Element.ALIGN_CENTER);
        pdf.addChild("Total Points: " + currentUser.getFocusPoints()[User.GAME_MEMORY_HARD], Element.ALIGN_CENTER);
        pdf.addChild("Games Played: " + currentUser.getGamesPlayed()[User.GAME_MEMORY_HARD], Element.ALIGN_CENTER);
        pdf.addChild("Play Time: " + TimeFormatter.autoFormatTime(currentUser.getPlayTime()[User.GAME_MEMORY_HARD]), Element.ALIGN_CENTER);
        pdf.addChild("High Score: " + TimeFormatter.autoFormatTime(currentUser.getHighScores()[User.GAME_MEMORY_HARD]), Element.ALIGN_CENTER);
        pdf.addChild("Accuracy: " + currentUser.getAccuracy()[User.GAME_MEMORY_HARD], Element.ALIGN_CENTER);
        pdf.addParagraph();
        headers = new ArrayList<>();
        lines = new ArrayList<>();
        headers2 = new ArrayList<>();
        lines2 = new ArrayList<>();
        i = 0;
        for (Number[] game : currentUser.getLastGames()[User.GAME_MEMORY_HARD]) {
            if ((int) ((i + 1) / 3) == 0) {
                headers.add("Game " + (currentUser.getLastGames()[User.GAME_MEMORY_HARD].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.POINTS];
                ls[1] = "Accuracy: " + game[User.ACC];
                ls[2] = "Time: " + game[User.TIME];
                lines.add(ls);
            } else {
                headers2.add("Game" + (currentUser.getLastGames()[User.GAME_MEMORY_HARD].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.POINTS];
                ls[1] = "Accuracy: " + game[User.ACC];
                ls[2] = "Time: " + game[User.TIME];
                lines2.add(ls);
            }
            i++;
        }
        pdf.createTable(headers, lines, Math.min(3, lines.size()));
        if (!lines2.isEmpty()) pdf.createTable(headers2, lines2, Math.min(2, lines2.size()));
        pdf.addHeader("Sorting Game", Element.ALIGN_CENTER);
        pdf.addParagraph();
        pdf.addSubheader("Slow", Element.ALIGN_CENTER);
        pdf.addChild("Total Points: " + currentUser.getFocusPoints()[User.GAME_SORTING_SLOW], Element.ALIGN_CENTER);
        pdf.addChild("Games Played: " + currentUser.getGamesPlayed()[User.GAME_SORTING_SLOW], Element.ALIGN_CENTER);
        pdf.addChild("Play Time: " + TimeFormatter.autoFormatTime(currentUser.getPlayTime()[User.GAME_SORTING_SLOW]), Element.ALIGN_CENTER);
        pdf.addChild("High Score: " + TimeFormatter.autoFormatTime(currentUser.getHighScores()[User.GAME_SORTING_SLOW]), Element.ALIGN_CENTER);
        pdf.addChild("Accuracy: " + currentUser.getAccuracy()[User.GAME_SORTING_SLOW], Element.ALIGN_CENTER);
        pdf.addParagraph();
        headers = new ArrayList<>();
        lines = new ArrayList<>();
        headers2 = new ArrayList<>();
        lines2 = new ArrayList<>();
        i = 0;
        for (Number[] game : currentUser.getLastGames()[User.GAME_SORTING_SLOW]) {
            if ((int) ((i + 1) / 3) == 0) {
                headers.add("Game " + (currentUser.getLastGames()[User.GAME_SORTING_SLOW].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.POINTS];
                ls[1] = "Accuracy: " + game[User.ACC];
                ls[2] = "Time: " + game[User.TIME];
                lines.add(ls);
            } else {
                headers2.add("Game" + (currentUser.getLastGames()[User.GAME_SORTING_SLOW].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.POINTS];
                ls[1] = "Accuracy: " + game[User.ACC];
                ls[2] = "Time: " + game[User.TIME];
                lines2.add(ls);
            }
            i++;
        }
        pdf.createTable(headers, lines, Math.min(3, lines.size()));
        if (!lines2.isEmpty()) pdf.createTable(headers2, lines2, Math.min(2, lines2.size()));
        pdf.addSubheader("Fast", Element.ALIGN_CENTER);
        pdf.addChild("Total Points: " + currentUser.getFocusPoints()[User.GAME_SORTING_FAST], Element.ALIGN_CENTER);
        pdf.addChild("Games Played: " + currentUser.getGamesPlayed()[User.GAME_SORTING_FAST], Element.ALIGN_CENTER);
        pdf.addChild("Play Time: " + TimeFormatter.autoFormatTime(currentUser.getPlayTime()[User.GAME_SORTING_FAST]), Element.ALIGN_CENTER);
        pdf.addChild("High Score: " + TimeFormatter.autoFormatTime(currentUser.getHighScores()[User.GAME_SORTING_FAST]), Element.ALIGN_CENTER);
        pdf.addChild("Accuracy: " + currentUser.getAccuracy()[User.GAME_SORTING_FAST], Element.ALIGN_CENTER);
        pdf.addParagraph();
        headers = new ArrayList<>();
        lines = new ArrayList<>();
        headers2 = new ArrayList<>();
        lines2 = new ArrayList<>();
        i = 0;
        for (Number[] game : currentUser.getLastGames()[User.GAME_SORTING_FAST]) {
            if ((int) ((i + 1) / 3) == 0) {
                headers.add("Game " + (currentUser.getLastGames()[User.GAME_SORTING_FAST].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.POINTS];
                ls[1] = "Accuracy: " + game[User.ACC];
                ls[2] = "Time: " + game[User.TIME];
                lines.add(ls);
            } else {
                headers2.add("Game" + (currentUser.getLastGames()[User.GAME_SORTING_FAST].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.POINTS];
                ls[1] = "Accuracy: " + game[User.ACC];
                ls[2] = "Time: " + game[User.TIME];
                lines2.add(ls);
            }
            i++;
        }
        pdf.createTable(headers, lines, Math.min(3, lines.size()));
        if (!lines2.isEmpty()) pdf.createTable(headers2, lines2, Math.min(2, lines2.size()));
        pdf.addHeader("Strategy Game", Element.ALIGN_CENTER);
        pdf.addParagraph();
        pdf.addSubheader("Easy", Element.ALIGN_CENTER);
        pdf.addChild("Total Points: " + currentUser.getFocusPoints()[User.GAME_STRATEGY_EASY], Element.ALIGN_CENTER);
        pdf.addChild("Games Played: " + currentUser.getGamesPlayed()[User.GAME_STRATEGY_EASY], Element.ALIGN_CENTER);
        pdf.addChild("Play Time: " + TimeFormatter.autoFormatTime(currentUser.getPlayTime()[User.GAME_STRATEGY_EASY]), Element.ALIGN_CENTER);
        pdf.addChild("Average Focus Points: " + currentUser.getHighScores()[User.GAME_STRATEGY_EASY - 6], Element.ALIGN_CENTER);
        pdf.addParagraph();
        headers = new ArrayList<>();
        lines = new ArrayList<>();
        headers2 = new ArrayList<>();
        lines2 = new ArrayList<>();
        i = 0;
        for (Number[] game : currentUser.getLastGames()[User.GAME_STRATEGY_EASY]) {
            if ((int) ((i + 1) / 3) == 0) {
                headers.add("Game " + (currentUser.getLastGames()[User.GAME_STRATEGY_EASY].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.STRAT_POINTS];
                ls[1] = "Moves: " + game[User.STRAT_MOVES];
                ls[2] = "Pieces Left: " + game[User.STRAT_PIECES_LEFT];
                lines.add(ls);
            } else {
                headers2.add("Game" + (currentUser.getLastGames()[User.GAME_STRATEGY_EASY].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.STRAT_POINTS];
                ls[1] = "Moves: " + game[User.STRAT_MOVES];
                ls[2] = "Pieces Left: " + game[User.STRAT_PIECES_LEFT];
                lines.add(ls);
            }
            i++;
        }
        pdf.createTable(headers, lines, Math.min(3, lines.size()));
        if (!lines2.isEmpty()) pdf.createTable(headers2, lines2, Math.min(2, lines2.size()));
        pdf.addSubheader("Hard", Element.ALIGN_CENTER);
        pdf.addChild("Total Points: " + currentUser.getFocusPoints()[User.GAME_STRATEGY_HARD], Element.ALIGN_CENTER);
        pdf.addChild("Games Played: " + currentUser.getGamesPlayed()[User.GAME_STRATEGY_HARD], Element.ALIGN_CENTER);
        pdf.addChild("Play Time: " + TimeFormatter.autoFormatTime(currentUser.getPlayTime()[User.GAME_STRATEGY_HARD]), Element.ALIGN_CENTER);
        pdf.addChild("Average Focus Points: " + currentUser.getHighScores()[User.GAME_STRATEGY_HARD - 6], Element.ALIGN_CENTER);
        pdf.addParagraph();
        headers = new ArrayList<>();
        lines = new ArrayList<>();
        headers2 = new ArrayList<>();
        lines2 = new ArrayList<>();
        i = 0;
        for (Number[] game : currentUser.getLastGames()[User.GAME_STRATEGY_HARD]) {
            if ((int) ((i + 1) / 3) == 0) {
                headers.add("Game " + (currentUser.getLastGames()[User.GAME_STRATEGY_HARD].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.STRAT_POINTS];
                ls[1] = "Moves: " + game[User.STRAT_MOVES];
                ls[2] = "Pieces Left: " + game[User.STRAT_PIECES_LEFT];
                lines.add(ls);
            } else {
                headers2.add("Game" + (currentUser.getLastGames()[User.GAME_STRATEGY_HARD].indexOf(game) + 1));
                String[] ls = new String[User.statsTypes];
                ls[0] = "Points: " + game[User.STRAT_POINTS];
                ls[1] = "Moves: " + game[User.STRAT_MOVES];
                ls[2] = "Pieces Left: " + game[User.STRAT_PIECES_LEFT];
                lines.add(ls);
            }
            i++;
        }
        pdf.createTable(headers, lines, Math.min(3, lines.size()));
        if (!lines2.isEmpty()) pdf.createTable(headers2, lines2, Math.min(2, lines2.size()));

        pdf.closeDocument();



        if(isExternalStorageWritable()){
            pdf.viewPDF(this);

        }else{
            Log.i("", "not writable");
        }



    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    private String createName(Calendar now, String name) {
        return "Bullseye_" + name + "_" + (now.get(Calendar.MONTH) + 1) + "_" +
                now.get(Calendar.DAY_OF_MONTH) + "_" +
                now.get(Calendar.YEAR) + "_" +
                now.get(Calendar.HOUR_OF_DAY) + ":" +
                now.get(Calendar.MINUTE) + ":" +
                now.get(Calendar.SECOND) +
                ".pdf";
    }

}