package com.example.bullseye_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.Fetcher;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.util.UserAdapter;

public class MoreUsersActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private LiveData<User> first;
    private UserAdapter userAdapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_users);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userAdapter = new UserAdapter(this);
        rv = findViewById(R.id.rv);

        rv.setAdapter(userAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        first = userViewModel.getUser(getSharedPreferences("userID", 0).getLong("id", 0));

        first.observe(this, user -> {
            userAdapter.setFirst(user);
        });


    }

    public void startUser(User user) {
        getSharedPreferences("userID", 0).edit().putLong("id", user.getId()).apply();
        startActivity(new Intent(this, UserDashboardActivity.class));
        finish();
    }

}