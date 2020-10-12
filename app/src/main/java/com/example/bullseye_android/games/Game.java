package com.example.bullseye_android.games;

import androidx.fragment.app.FragmentManager;

public interface Game {
    void unpause();

    FragmentManager getSupportFragmentManager();

    String getGame();
}
