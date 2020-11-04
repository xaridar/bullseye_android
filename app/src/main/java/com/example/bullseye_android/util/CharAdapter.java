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
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class CharAdapter extends RecyclerView.Adapter<CharAdapter.CharViewHolder> {

    private LayoutInflater mInflater;
    private List<CharDescriber> chars;

    public CharAdapter(AppCompatActivity activity, List<CharDescriber> chars) {
        mInflater = LayoutInflater.from(activity);
        this.chars = chars;
    }

    @NonNull
    @Override
    public CharViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.character_card, parent, false);
        Log.i("HH", parent.toString());
        return new CharViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CharViewHolder holder, int position) {
        CharDescriber charDescriber = chars.get(position);
        holder.name.setText(charDescriber.name);
        holder.desc.setText(charDescriber.description);
        holder.img.setImageResource(charDescriber.resource);
    }

    @Override
    public int getItemCount() {
        return chars.size();
    }

    public static class CharViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView img;
        private TextView desc;

        public CharViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.charName);
            desc = itemView.findViewById(R.id.charDesc);
            img = itemView.findViewById(R.id.charImg);
        }
    }
}
