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
import android.view.MenuItem;
import android.widget.Toast;

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

        Intent intent = getIntent();

        //0: UsersActivity, 1: ManageUsers, 2: Bad
        int context;

        if(intent != null){
            context = intent.getIntExtra("MoreUsersContext", 2);
        }else{
            context = 2;
        }
        switch (context){
            case 0:
                getSupportActionBar().setTitle("All Users");
                break;
            case 1:
                getSupportActionBar().setTitle("All Users (Hold User to Delete)");
                break;
            default:
                getSupportActionBar().setTitle("ERROR, activity context empty or invalid");
                break;
        }
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        if(context == 0 || context ==1){
            userAdapter = new UserAdapter(this, context);
            rv = findViewById(R.id.rv);

            rv.setAdapter(userAdapter);
            rv.setLayoutManager(new LinearLayoutManager(this));

            first = userViewModel.getUser(getSharedPreferences("userID", 0).getLong("id", 0));

            first.observe(this, user -> {
                userAdapter.setFirst(user);
            });
        }else{
            Toast.makeText(this, "ERROR: Not coming from mem_correct page (Users Page / Manage Users Page)",Toast.LENGTH_SHORT).show();
        }

    }

    public void startUser(User user) {
        getSharedPreferences("userID", 0).edit().putLong("id", user.getId()).apply();
        startActivity(new Intent(this, UserDashboardActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}