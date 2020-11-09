package com.example.bullseye_android.games.turn_based.units;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Tile;
import com.example.bullseye_android.games.turn_based.TurnBasedActivity;

public class PlayerUnit extends Unit {
    public PlayerUnit(String name, int x, int y, String icon, int movespeed, Owners owner, Tile[][] board, Node[][] graph, LifecycleOwner ctx, TurnBasedActivity game) {
        super(name, x, y, icon, movespeed, owner, board, graph, ctx, game);
    }
}
