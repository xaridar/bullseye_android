// Elliot wrote
package com.example.bullseye_android.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bullseye_android.R;
import com.example.bullseye_android.activities.StatsActivity;
import com.example.bullseye_android.database.User;

import java.util.ArrayList;
import java.util.List;

public class NavAdapter extends RecyclerView.Adapter<NavAdapter.NavViewHolder> {
    private final LayoutInflater mInflater;
    private List<User> users = new ArrayList<>();
    private Context context;
    private User current;

    public NavAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setUsers(List<User> users) {
        if (users != null) {
            for (User user : users) {
                if (!user.isAdmin()) {
                    this.users.add(user);
                }
            }

        } else {
            this.users = new ArrayList<>();
        }
        if (this.users.size() > 0) {
            if (current == null) {
                current = this.users.get(0);
            }
            removeUser(current);
            ((StatsActivity) context).changeFirst(current);
        }
    }

    @NonNull
    @Override
    public NavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.nav_item, parent, false);
        return new NavViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NavViewHolder holder, int position) {
        final User user = users.get(position);
        holder.image.setImageResource(context.getResources().getIdentifier("pfp_" + user.getAvatar(), "drawable", "com.example.bullseye_android"));
        holder.nameText.setText(user.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser(current);
                current = user;
                removeUser(current);
                ((StatsActivity) context).changeFirst(current);
            }
        });
    }

    public void removeUser(User name) {
        users.remove(name);
        notifyDataSetChanged();
    }

    public void addUser(User name) {
        users.add(name);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public User getCurrent() {
        return current;
    }

    static class NavViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private ImageView image;

        private NavViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameView);
            image = itemView.findViewById(R.id.imageView);
        }
    }
}
