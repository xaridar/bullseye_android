// Elliot coded, Dylan designed
package com.example.bullseye_android.games;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bullseye_android.R;
import com.example.bullseye_android.activities.UsersSettingsActivity;
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserSerializable;
import com.example.bullseye_android.util.SfxManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class GamePauseFragment extends Fragment {

    private static final String ARG_USER = "user";

    User user;
    ImageButton button;
    TextView text;
    MaterialButton finish;
    int time;
    Timer timer;
    Game ctx;
    FloatingActionButton settings;
    private MediaPlayer countdown;
    private MediaPlayer countdownfinish;

    public GamePauseFragment() { }

    public static GamePauseFragment newInstance(User user) {
        GamePauseFragment fragment = new GamePauseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, new UserSerializable(user));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_pause, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getContext() != null) {
            countdown = SfxManager.createSfx(getContext(), R.raw.countdown, (float) user.getGameVolume() / User.MAX_VOLUME);
            countdownfinish = SfxManager.createSfx(getContext(), R.raw.countdownfinish, (float) user.getGameVolume() / User.MAX_VOLUME);
            ctx = (Game) getContext();
            time = 3;
            button = view.findViewById(R.id.unpauseButton);
            text = view.findViewById(R.id.countdown);
            button.setOnClickListener(this::back);
            finish = view.findViewById(R.id.finish);
            finish.setOnClickListener(this::finish);
            settings = view.findViewById(R.id.settingsfab);
            reset();
            switch (ctx.getGame()) {
                case "matching":
                    settings.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.memCardColor2)));
                    settings.setImageTintList(ColorStateList.valueOf(getContext().getColor(R.color.memCardColor1)));
                    settings.setRippleColor(getContext().getColor(R.color.memCardColor1));
                    view.setBackgroundColor(getContext().getColor(R.color.memBackground));
                    button.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.memCardColor1)));
                    finish.setTextColor(getContext().getColor(R.color.memText));
                    finish.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.memCardColor1)));
                    break;
                case "sorting":
                    settings.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.sortingRight)));
                    settings.setImageTintList(ColorStateList.valueOf(getContext().getColor(R.color.sortingLeft)));
                    settings.setRippleColor(getContext().getColor(R.color.sortingLeft));
                    view.setBackgroundColor(getContext().getColor(R.color.sortingBackground));
                    button.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.sortingRight)));
                    finish.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.sortingRight)));
                    finish.setTextColor(getContext().getColor(R.color.color1));
                    break;
                case "strategy":
                    settings.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.tbSecondary)));
                    settings.setImageTintList(ColorStateList.valueOf(getContext().getColor(R.color.tbPrimary)));
                    settings.setRippleColor(getContext().getColor(R.color.tbBackground));
                    view.setBackgroundColor(getContext().getColor(R.color.tbBackground));
                    button.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.tbSecondary)));
                    finish.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.tbSecondary)));
                    finish.setTextColor(getContext().getColor(R.color.tbText));
                    break;
                default:
                    break;
            }
            settings.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), UsersSettingsActivity.class);
                StringBuilder name = new StringBuilder();
                String game = ctx.getGame();
                name.append(Character.toUpperCase(game.charAt(0)));
                game = game.substring(1);
                name.append(game);
                name.append(" Game");
                intent.putExtra("game", name.toString());
                startActivity(intent);
            });
        }
    }

    void reset() {
        button.setVisibility(View.VISIBLE);
        text.setVisibility(View.INVISIBLE);
    }

    public void back(View view) {
        Activity act = getActivity();
        if (getContext() instanceof Game && act != null){
            text.setTextSize(250);
            button.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
            countdown.start();
            text.setText(String.valueOf(time));
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    time--;
                    if (time == 0){
                        cancel();
                        countdownfinish.start();
                        act.runOnUiThread(()-> {
                            text.setTextSize(80);
                            text.setText(getString(R.string.start).toUpperCase());
                        });
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                exitPauseMenu();
                            }
                        }, 1000);
                    }else{
                        countdown.start();
                        act.runOnUiThread(() -> text.setText(String.valueOf(time)));
                    }
                }
            }, 1000, 1000);
            if (time == 0) {
                exitPauseMenu();
            }

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null){
            timer.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
        reset();
    }

    public void exitPauseMenu(){
        if (timer != null) {
            timer.cancel();
        }
        ctx.unpause();
        ctx.getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public void finish(View view) {
        Activity act = getActivity();
        if (act != null) getActivity().finish();
    }

}