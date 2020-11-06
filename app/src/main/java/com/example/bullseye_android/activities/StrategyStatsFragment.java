//coded and laid out by Aakash Sell
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
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserSerializable;
import com.example.bullseye_android.util.TimeFormatter;

public class StrategyStatsFragment extends Fragment {

    private static final String ARG_USER = "user";

    private User user;

    public StrategyStatsFragment() {
        // Required empty public constructor
    }

    public static StrategyStatsFragment newInstance(User u) {
        StrategyStatsFragment fragment = new StrategyStatsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, new UserSerializable(u));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UserSerializable us = (UserSerializable) getArguments().getSerializable(ARG_USER);
            if (us != null) {
                user = us.getUser();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        run(view);
    }

    public void run (View view) {
        TextView easy_games_played = view.findViewById(R.id.easy_games_played);
        TextView easy_ppg = view.findViewById(R.id.easy_ppg);
        TextView easy_total_time = view.findViewById(R.id.easy_average_time);
        TextView hard_games_played = view.findViewById(R.id.hard_games_played);
        TextView hard_ppg = view.findViewById(R.id.hard_ppg);
        TextView hard_total_time = view.findViewById(R.id.hard_average_time);
        TextView points = view.findViewById(R.id.points);
        points.setText(String.valueOf(user.getFocusPoints()[User.ALL_GAMES]));
        easy_games_played.setText(getString(R.string.games_played, user.getGamesPlayed()[User.GAME_STRATEGY_EASY] + ""));
        easy_ppg.setText(getString(R.string.avg_ppg, user.getStratPointsPerGame()[User.GAME_STRATEGY_EASY - 6] + ""));
        easy_total_time.setText(getString(R.string.average_time, TimeFormatter.autoFormatTime(user.getPlayTime()[User.GAME_STRATEGY_EASY])));
        hard_games_played.setText(getString(R.string.games_played, user.getGamesPlayed()[User.GAME_STRATEGY_HARD] + ""));
        hard_ppg.setText(getString(R.string.avg_ppg, user.getStratPointsPerGame()[User.GAME_STRATEGY_HARD - 6] + ""));
        hard_total_time.setText(getString(R.string.average_time, TimeFormatter.autoFormatTime(user.getPlayTime()[User.GAME_STRATEGY_HARD])));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_strategy, container, false);
    }
}