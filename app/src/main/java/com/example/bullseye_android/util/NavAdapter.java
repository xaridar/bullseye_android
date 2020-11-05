package com.example.bullseye_android.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import com.example.bullseye_android.activities.StatsActivity;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class NavAdapter extends RecyclerView.Adapter<NavAdapter.NavViewHolder> {

    private LayoutInflater mInflater;
    private LiveData<List<User>> users;
    private List<User> activeUsers = new ArrayList<>();
    private Context ctx;
    private User first;

    public NavAdapter(AppCompatActivity activity) {
        mInflater = LayoutInflater.from(activity);
        UserViewModel userViewModel = ViewModelProviders.of(activity).get(UserViewModel.class);
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
            } else {
                if(activeUsers.size() > 0){
                    ((StatsActivity) activity).changeFirst(activeUsers.get(0));
                }
            }
            notifyDataSetChanged();
        });
        ctx = activity;
    }

    @NonNull
    @Override
    public NavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.nav_item, parent, false);
        Log.i("HH", parent.toString());
        return new NavViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NavViewHolder holder, int position) {
        if (position == 0) {
            ((CardView) holder.itemView).setCardBackgroundColor(ctx.getColor(R.color.color2));
            CardView.LayoutParams params = new CardView.LayoutParams(dpToPixels(60), dpToPixels(60));
            params.setMargins(dpToPixels(15), dpToPixels(10), dpToPixels(50), dpToPixels(10));
            holder.avatar.setLayoutParams(params);

            holder.name.setTextColor(ctx.getColor(R.color.color5));
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        }
        User user = activeUsers.get(position);
        holder.name.setText(user.getName());
        holder.avatar.setImageResource(ctx.getResources().getIdentifier("pfp_" + user.getAvatar(), "drawable", "com.example.bullseye_android"));
        holder.itemView.setOnClickListener(view -> ((StatsActivity) ctx).changeFirst(user));
    }

    @Override
    public int getItemCount() {
        return activeUsers.size();
    }

    public void setFirst(User first) {
        this.first = first;
        if (users.getValue() != null && this.first != null) {
            List<User> remove = new ArrayList<>();
            for (User user : users.getValue()) {
                if (user.isAdmin() || user.getId() == this.first.getId()) {
                    remove.add(user);
                }
            }
            users.getValue().removeAll(remove);
            activeUsers.add(0, this.first);
            notifyDataSetChanged();
        }
    }

    private int dpToPixels(int dp) {
        return dp * (ctx.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static class NavViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView avatar;

        public NavViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameView);
            avatar = itemView.findViewById(R.id.imageView);
        }
    }
}
