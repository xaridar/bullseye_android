package com.example.bullseye_android.games;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bullseye_android.R;
import com.example.bullseye_android.games.memory.MemoryActivity;

import java.util.Timer;
import java.util.TimerTask;

public class GamePauseFragment extends Fragment {

    ImageButton button;
    TextView text;
    int time;
    Timer timer;

    public GamePauseFragment() { }

    public static GamePauseFragment newInstance() {
        return new GamePauseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_memory_pause, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        time = 3;
        button = view.findViewById(R.id.unpauseButton);
        text = view.findViewById(R.id.countdown);
        button.setOnClickListener(this::back);
        view.findViewById(R.id.finish).setOnClickListener(this::finish);
    }

    public void back(View view) {
        if (getContext() instanceof MemoryActivity){
            text.setTextSize(250);
            button.setVisibility(View.INVISIBLE);
            text.setText(String.valueOf(time));
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    time--;
                    if (time == 0){
                        cancel();
                        getActivity().runOnUiThread(()-> {
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
                        getActivity().runOnUiThread(() -> {
                            text.setText(String.valueOf(time));
                        });
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
        if (timer != null) {
            timer.cancel();
        }
    }

    public void exitPauseMenu(){
        ((Game) getContext()).unpause();
        ((Game) getContext()).getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public void finish(View view) {
        getActivity().finish();
    }
}