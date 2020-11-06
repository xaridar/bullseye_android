package com.example.bullseye_android.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bullseye_android.R;
import com.example.bullseye_android.activities.MoreUsersActivity;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private LayoutInflater mInflater;
    private UserViewModel userViewModel;
    private LiveData<List<User>> users;
    private List<User> activeUsers = new ArrayList<>();
    private Context ctx;
    private int ctxint;
    private User first;

    public UserAdapter(AppCompatActivity activity, int context) {
        mInflater = LayoutInflater.from(activity);
        userViewModel = ViewModelProviders.of(activity).get(UserViewModel.class);
        users = userViewModel.getUsers();
        users.observe(activity, users -> {
            List<User> remove = new ArrayList<>();
            for (User user : users) {
                if (user.isAdmin()) {
                    remove.add(user);
                } else if (this.first != null && user.getId() == this.first.getId()) {
                    remove.add(user);
                }
            }
            users.removeAll(remove);
            activeUsers = users;
            if (this.first != null) {
                activeUsers.add(0, this.first);
            }
            notifyDataSetChanged();
        });
        ctx = activity;
        ctxint = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.user_cardview, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (position == 0 && ctxint == 0) {
            ((CardView) holder.itemView).setCardBackgroundColor(ctx.getColor(R.color.color2));
        }
        User user = activeUsers.get(position);
        holder.name.setText(user.getName());
        holder.avatar.setImageResource(ctx.getResources().getIdentifier("pfp_" + user.getAvatar(), "drawable", "com.example.bullseye_android"));
        holder.itemView.setOnClickListener(view -> {
            if (ctxint == 0) {
                ((MoreUsersActivity) ctx).startUser(user);
            }
        });
        holder.itemView.setOnLongClickListener(view -> {
            if (ctxint == 1) {
                userViewModel.remove(user);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return activeUsers.size();
    }

    public void setFirst(User first) {
        this.first = first;
        if (users.getValue() != null) {
            List<User> remove = new ArrayList<>();
            if (users.getValue() != null) {
                for (User user : users.getValue()) {
                    if (user != null) {
                        if (user.isAdmin() || user.getId() == this.first.getId()){
                            remove.add(user);
                        }
                    }
                }
            }
            if (users.getValue() != null) users.getValue().removeAll(remove);
            activeUsers.add(0, this.first);
            notifyDataSetChanged();
        }
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView avatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
