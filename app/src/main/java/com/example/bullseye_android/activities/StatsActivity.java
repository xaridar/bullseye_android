// all Stats activity design was done by Elliot
package com.example.bullseye_android.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.util.NavAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class StatsActivity extends AppCompatActivity {
    /*
    Commented out code is for use with database - next to lines to be removed when database is implemented
     */

    public static final int MEMORY = 0;
    public static final int SORTING = 1;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private MutableLiveData<User> currentUser;
    private MutableLiveData<Integer> tab;
    private TextView memory;
    private TextView sorting;
    private List<TextView> bottomNav = new ArrayList<>();
    /* replacement point */
    private List<User> users;
//     private LiveData<List<User>> users;
    private UserViewModel mUserViewModel;
    private ExtendedFloatingActionButton exportFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        /* replacement point */
        users = new ArrayList<>(Arrays.asList(new User("Chuck", 1, "archer"), new User("Chris", 2, "default"), new User("Beverly", 3, "boy"), new User("Ryan", 4, "girl")));
//        users = mUserViewModel.getUsers();
        /* set stats for example - removal point */
        users.get(0).addGame(User.GAME_MEMORY_HARD, .43f, 34, 56);

        currentUser = new MutableLiveData<>();

        Function<User, Void> callback = user -> {
            currentUser.setValue(user);
            run();
            return null;
        };

        /* replacement point */
        callback.apply(users.get(0));
//        Fetcher.runNewUserFetcher(mUserViewModel, this, getSharedPreferences("userID", 0).getLong("id", 0), callback);

    }

    private void run() {

        tab = new MutableLiveData<>();
        RecyclerView rv = findViewById(R.id.rv);
        toolbar = findViewById(R.id.toolbar);
        Toolbar navtoolbar = findViewById(R.id.navtoolbar);
        drawerLayout = findViewById(R.id.drawer);
        CardView current = findViewById(R.id.current);
        sorting = findViewById(R.id.sorting_bottom);
        memory = findViewById(R.id.memory_bottom);
        exportFab = findViewById(R.id.exportFab);

        bottomNav.add(sorting);
        bottomNav.add(memory);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ActionBarDrawerToggle navActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, navtoolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(navActionBarDrawerToggle);
        navActionBarDrawerToggle.syncState();

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getColor(android.R.color.white));
        navActionBarDrawerToggle.getDrawerArrowDrawable().setColor(getColor(android.R.color.white));


        /* replacement point */
//        if (currentUser.getValue() == null) {
//            if (users.getValue() != null) {
//                currentUser.setValue(users.getValue().get(0));
//            }
//        }
//        final NavAdapter adapter = new NavAdapter(this, users.getValue(), currentUser.getValue());
        final NavAdapter adapter = new NavAdapter(this, users, currentUser.getValue() == null ? currentUser.getValue() : users.get(0));

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        current.setOnClickListener(view -> {
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        currentUser.observe(this, user -> {
            if (user != null) {
                drawerLayout.closeDrawer(GravityCompat.START);
                ((TextView) findViewById(R.id.nameView)).setText(user.getName());
                ((ImageView) findViewById(R.id.imageView)).setImageResource(getResources().getIdentifier("pfp_" + user.getAvatar(), "drawable", "com.example.bullseye_android"));
                toolbar.setTitle(getString(R.string.stats_header, user.getName()));

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

        /* addition point */
//        users.observe(this, adapter::setUsers);

        memory.setOnClickListener(view -> {
            tab.setValue(MEMORY);
        });

        sorting.setOnClickListener(view -> {
            tab.setValue(SORTING);
        });

        exportFab.setOnClickListener(view -> {
            User user = currentUser.getValue();
            if (user != null) {
                // create String with all stats
                export(user.getName(), "Total Points: " + user.getFocusPoints()[User.ALL_GAMES]);
            }
        });
    }

    public void changeFirst(User current) {
        this.currentUser.setValue(current);
    }

    public void back(View view) {
        finish();
    }

    public void export(String name, String message) {
            String mimeType = "text/plain";
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(mimeType)
                    .setText(message)
                    .setChooserTitle("Share " + name + "'s stats")
                    .setSubject(name + "'s stats from Bullseye app")
                    .startChooser();
    }
}