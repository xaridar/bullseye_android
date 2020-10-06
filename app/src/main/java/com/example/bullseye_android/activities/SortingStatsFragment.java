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

public class SortingStatsFragment extends Fragment {

    private static final String ARG_USER = "user";

    private User user;

    public SortingStatsFragment() {
        // Required empty public constructor
    }

    public static SortingStatsFragment newInstance(User u) {
        SortingStatsFragment fragment = new SortingStatsFragment();
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
        TextView slow_games_played = view.findViewById(R.id.slow_games_played);
        TextView slow_high_score = view.findViewById(R.id.slow_high_score);
        TextView slow_average_time = view.findViewById(R.id.slow_average_time);
        TextView slow_accuracy = view.findViewById(R.id.slow_accuracy);
        TextView fast_games_played = view.findViewById(R.id.fast_games_played);
        TextView fast_high_score = view.findViewById(R.id.fast_high_score);
        TextView fast_average_time = view.findViewById(R.id.fast_average_time);
        TextView fast_accuracy = view.findViewById(R.id.fast_accuracy);
        TextView points = view.findViewById(R.id.points);
        points.setText(user.getFocusPoints()[User.ALL_GAMES] + "");
        slow_games_played.setText(getString(R.string.games_played, user.getGamesPlayed()[User.GAME_SORTING_SLOW] + ""));
        slow_high_score.setText(getString(R.string.high_score_stats, user.getHighScores()[User.GAME_SORTING_SLOW] + ""));
        slow_average_time.setText(getString(R.string.average_time, user.getPlayTime()[User.GAME_SORTING_SLOW] + ""));
        slow_accuracy.setText(getString(R.string.accuracy, user.getAccuracy()[User.GAME_SORTING_SLOW] + ""));
        fast_games_played.setText(getString(R.string.games_played, user.getGamesPlayed()[User.GAME_SORTING_FAST] + ""));
        fast_high_score.setText(getString(R.string.high_score_stats, user.getHighScores()[User.GAME_SORTING_FAST] + ""));
        fast_average_time.setText(getString(R.string.average_time, user.getPlayTime()[User.GAME_SORTING_FAST] + ""));
        fast_accuracy.setText(getString(R.string.accuracy, user.getAccuracy()[User.GAME_SORTING_FAST] + ""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_sorting, container, false);
    }
}