// Aakash coded and created layout, Elliot coded filling with users
package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.user.Fetcher;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.notifs.Notifications;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private View.OnClickListener avatarListener = v -> {
        long contentDescription = Long.parseLong(v.getContentDescription().toString());
        SharedPreferences sharedPreferences = getSharedPreferences("userID", 0);
        sharedPreferences.edit().putLong("id", contentDescription).apply();
        Intent myIntent = new Intent(v.getContext(), UserDashboardActivity.class);
        startActivity(myIntent);
    };
    private User admin;
    private LiveData<User> lastUser;
    private LiveData<List<User>> users;
    private LinearLayout userLayout;
    private int c;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Notifications notifications = new Notifications(this);
        notifications.createNotification(this,"User", "Good Job you got into the users screen", this.getClass());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        UserViewModel mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        users = mUserViewModel.getUsers();

        c = 0;

        Fetcher.runNewAdminFetcher(mUserViewModel, user -> {
            if (user == null) {
                Toast.makeText(this, "No admin account found. Please make one.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UsersActivity.this, AdminSignUpActivity.class));
                finish();
            } else {
                admin = user;
                c++;
                checkForRun();
            }
            return null;
        });

        Fetcher.runNewUserFetcher(mUserViewModel, this, getSharedPreferences("userID", 0).getLong("id", 0), user -> {
            lastUser = user;
            c++;
            checkForRun();
            return null;
        });
    }

    private void checkForRun() {
        if (c == 2) {
            run();
        }
    }

    public void run(){

        userLayout = findViewById(R.id.userLayout);

        MaterialButton adminBtn = findViewById(R.id.adminSignIn);

        button = findViewById(R.id.addUser);
        button.setOnClickListener(v -> {
            Intent myIntent = new Intent(v.getContext(), UserSignUpActivity.class);
            startActivity(myIntent);
        });

        users.observe(this, this::createLayout);

        lastUser.observe(this, user -> {
            if (users.getValue() != null) {
                createLayout(users.getValue());
            }
        });

        adminBtn.setText(getString(R.string.admin_btn, admin.getName()));
        adminBtn.setOnClickListener(v -> {

            Intent myIntent = new Intent(v.getContext(), AdminSignInActivity.class);
            startActivity(myIntent);
        });

    }

    private void createLayout(List<User> givenUsers) {
        List<User> removeList = new ArrayList<>();
        for (User user : givenUsers) {
            if (user.isAdmin()) {
                removeList.add(user);
                break;
            }
        }
        givenUsers.removeAll(removeList);
        userLayout.removeAllViews();
        LinearLayout inner = null;
        int viewsPerRow;
        boolean greater = false;
        if (lastUser.getValue() != null) {
            User first = null;
            for (User user : givenUsers) {
                if (user.getId() == lastUser.getValue().getId()) {
                        first = givenUsers.get(givenUsers.indexOf(user));
                    break;
                }
            }
            givenUsers.remove(first);

            if (first != null) {
                givenUsers.add(0, first);
            }
        }
        int userNum = givenUsers.size();
        if (userNum <= 6) {
            viewsPerRow = Math.round((userNum / 2f) + .05f);
        } else {
            viewsPerRow = 3;
        }
        if (userNum > 9) {
            userNum = 8;
            givenUsers = givenUsers.subList(0, 8);
            greater = true;
        }
        for (int i = 0; i < givenUsers.size(); i++) {
            if (i % viewsPerRow == 0) {

                inner = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.weight = 1;
                inner.setLayoutParams(layoutParams);
                inner.setOrientation(LinearLayout.HORIZONTAL);
                userLayout.addView(inner);

            }

            LinearLayout specLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            specLayout.setLayoutParams(params);
            specLayout.setPadding(10, 10, 10, 10);
            specLayout.setOrientation(LinearLayout.VERTICAL);

            ImageButton avaBtn = new ImageButton(this);
            LinearLayout.LayoutParams avaParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            avaParams.weight = 1;
            avaParams.gravity = Gravity.CENTER_HORIZONTAL;
            avaBtn.setLayoutParams(avaParams);
            avaBtn.setBackgroundColor(getColor(android.R.color.transparent));
            avaBtn.setImageResource(getResources().getIdentifier("pfp_" + givenUsers.get(i).getAvatar(), "drawable", "com.example.bullseye_android"));
            avaBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            avaBtn.setContentDescription(String.valueOf(givenUsers.get(i).getId()));
            avaBtn.setOnClickListener(avatarListener);

            TextView nameTxt = new TextView(new ContextThemeWrapper(this, R.style.UsernameStyle));
            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtParams.gravity = Gravity.CENTER_HORIZONTAL;
            nameTxt.setLayoutParams(txtParams);
            nameTxt.setText(givenUsers.get(i).getName());
            if (userNum == 1) {
                nameTxt.setTextSize(20);
            } else {
                nameTxt.setTextSize(14);
            }

            specLayout.addView(avaBtn);
            specLayout.addView(nameTxt);
            inner.addView(specLayout);

        }
        if (greater) {

            LinearLayout specLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            specLayout.setLayoutParams(params);
            specLayout.setPadding(10, 10, 10, 10);
            specLayout.setOrientation(LinearLayout.VERTICAL);

            MaterialButton moreBtn = new MaterialButton(new ContextThemeWrapper(this, R.style.MoreUsersStyle));
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            btnParams.weight = 1;
            btnParams.gravity = Gravity.CENTER_HORIZONTAL;
            moreBtn.setLayoutParams(btnParams);
            Drawable circle = ContextCompat.getDrawable(this, R.drawable.ic_circle);
            if (circle != null) moreBtn.setBackground(circle);
            moreBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color4)));
            moreBtn.setText(R.string.more_users);
            moreBtn.setAllCaps(false);
            moreBtn.setTextColor(getColor(R.color.color1));
            moreBtn.setOnClickListener(view -> {
                Intent intent = new Intent(UsersActivity.this, MoreUsersActivity.class);
                intent.putExtra("MoreUsersContext", 0);
                startActivity(intent);
            });

            TextView text = new TextView(this);
            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtParams.gravity = Gravity.CENTER_HORIZONTAL;
            text.setLayoutParams(txtParams);

            specLayout.addView(moreBtn);
            specLayout.addView(text);
            if (inner != null) inner.addView(specLayout);
        }
        ConstraintLayout.LayoutParams params = ((ConstraintLayout.LayoutParams) button.getLayoutParams());
        if (userNum == 0) {
            params.setMargins(0, 0, 0, (int) convertDpToPixel(200, this));
        } else {
            params.setMargins(0, 0, 0, (int) convertDpToPixel(30, this));
        }
        button.setLayoutParams(params);
    }
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
