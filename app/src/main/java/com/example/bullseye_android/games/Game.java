package com.example.bullseye_android.games;

import android.view.View;

import androidx.fragment.app.FragmentManager;

public interface Game {
    void unpause();

    FragmentManager getSupportFragmentManager();

    String getGame();

    void pause(View view);
}
