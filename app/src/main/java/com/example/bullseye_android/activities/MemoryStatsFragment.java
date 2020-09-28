package com.example.bullseye_android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserSerializable;

public class MemoryStatsFragment extends Fragment {

    private static final String ARG_USER = "user";

    private User user;

    public MemoryStatsFragment() {
        // Required empty public constructor
    }

    public static MemoryStatsFragment newInstance(User u) {
        MemoryStatsFragment fragment = new MemoryStatsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, new UserSerializable(u));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = ((UserSerializable) getArguments().getSerializable(ARG_USER)).getUser();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        run(view);
    }

    public void run (View view) {
        ((TextView) view.findViewById(R.id.points)).setText(String.valueOf(user.getFocusPoints()[User.ALL_GAMES]));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_memory, container, false);
    }
}