// coded by Dylan and Elliot, laid out by Elliot
package com.example.bullseye_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.util.UserAdapter;

public class MoreUsersActivity extends AppCompatActivity implements MusicActivity {

    private UserAdapter userAdapter;
    int context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_users);

        Intent intent = getIntent();

        //0: UsersActivity, 1: ManageUsers, 2: Bad

        if(intent != null){
            context = intent.getIntExtra("MoreUsersContext", 2);
        }else{
            context = 2;
        }
        if (getSupportActionBar() == null) finish();
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
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        if(context == 0 || context ==1){
            userAdapter = new UserAdapter(this, context);
            RecyclerView rv = findViewById(R.id.rv);

            rv.setAdapter(userAdapter);
            rv.setLayoutManager(new LinearLayoutManager(this));

            LiveData<User> first = userViewModel.getUser(getSharedPreferences("userID", 0).getLong("id", 0));

            first.observe(this, user -> {
                if (user != null) {
                    userAdapter.setFirst(user);
                }
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

    @Override
    public int getMusicId() {
        if (context == 1) {
            return R.raw.adminsong;
        }
        return 0;
    }
}