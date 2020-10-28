//Coded and laid out by aakash sell
package com.example.bullseye_android.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserSerializable;
import com.example.bullseye_android.util.TimeFormatter;

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
        TextView easy_high_score = view.findViewById(R.id.easy_high_score);
        TextView easy_average_time = view.findViewById(R.id.easy_average_time);
        TextView easy_accuracy = view.findViewById(R.id.easy_accuracy);
        TextView medium_games_played = view.findViewById(R.id.medium_games_played);
        TextView medium_high_score = view.findViewById(R.id.medium_high_score);
        TextView medium_average_time = view.findViewById(R.id.medium_average_time);
        TextView medium_accuracy = view.findViewById(R.id.medium_accuracy);
        TextView difficult_games_played = view.findViewById(R.id.difficult_games_played);
        TextView difficult_high_score = view.findViewById(R.id.difficult_high_score);
        TextView difficult_average_time = view.findViewById(R.id.difficult_average_time);
        TextView difficult_accuracy = view.findViewById(R.id.difficult_accuracy);
        TextView points = view.findViewById(R.id.points);
        points.setText(String.valueOf(user.getFocusPoints()[User.ALL_GAMES]));
        easy_games_played.setText(getString(R.string.games_played, user.getGamesPlayed()[User.GAME_MEMORY_EASY] + ""));
        easy_high_score.setText(getString(R.string.high_score_stats, TimeFormatter.autoFormatTime(user.getHighScores()[User.GAME_MEMORY_EASY])));
        easy_average_time.setText(getString(R.string.average_time, TimeFormatter.autoFormatTime(user.getPlayTime()[User.GAME_MEMORY_EASY])));
        easy_accuracy.setText(getString(R.string.accuracy, user.getAccuracy()[User.GAME_MEMORY_EASY] + ""));
        medium_games_played.setText(getString(R.string.games_played, user.getGamesPlayed()[User.GAME_MEMORY_NORMAL] + ""));
        medium_high_score.setText(getString(R.string.high_score_stats, TimeFormatter.autoFormatTime(user.getHighScores()[User.GAME_MEMORY_NORMAL])));
        medium_average_time.setText(getString(R.string.average_time, TimeFormatter.autoFormatTime(user.getPlayTime()[User.GAME_MEMORY_NORMAL])));
        medium_accuracy.setText(getString(R.string.accuracy, user.getAccuracy()[User.GAME_MEMORY_NORMAL] + ""));
        difficult_games_played.setText(getString(R.string.games_played, user.getGamesPlayed()[User.GAME_MEMORY_HARD] + ""));
        difficult_high_score.setText(getString(R.string.high_score_stats, TimeFormatter.autoFormatTime(user.getHighScores()[User.GAME_MEMORY_HARD])));
        difficult_average_time.setText(getString(R.string.average_time, TimeFormatter.autoFormatTime(user.getPlayTime()[User.GAME_MEMORY_HARD])));
        difficult_accuracy.setText(getString(R.string.accuracy, user.getAccuracy()[User.GAME_MEMORY_HARD] + ""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_memory, container, false);
    }
}