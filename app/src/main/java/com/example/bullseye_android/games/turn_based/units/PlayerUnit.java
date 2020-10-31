package com.example.bullseye_android.games.turn_based.units;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Tile;

public class PlayerUnit extends Unit {
    public PlayerUnit(String name, int x, int y, String icon, int movespeed, Owners owner, Tile[][] board, LifecycleOwner ctx) {
        super(name, x, y, icon, movespeed, owner, board, ctx);
    }
}
