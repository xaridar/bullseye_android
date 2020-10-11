package com.example.bullseye_android.games.memory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bullseye_android.R;
import com.example.bullseye_android.games.sorting.SortingCopy;
import com.example.bullseye_android.util.TimeFormatter;

import java.util.Timer;
import java.util.TimerTask;

public class MemoryPauseFragment extends Fragment {

    ImageButton button;
    TextView text;
    int time;
    Timer timer;

    public MemoryPauseFragment() { }

    public static MemoryPauseFragment newInstance() {
        return new MemoryPauseFragment();
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
        ((MemoryActivity) getContext()).unpause();
        ((MemoryActivity) getContext()).getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}