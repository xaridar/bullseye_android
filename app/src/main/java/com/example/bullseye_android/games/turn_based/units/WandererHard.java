package com.example.bullseye_android.games.turn_based.units;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Tile;
import com.example.bullseye_android.games.turn_based.TurnBasedActivity;

public class WandererHard extends Wanderer {
    public WandererHard(String name, int x, int y, String icon, int movespeed, Node[][] graph, Tile[][] board, LifecycleOwner ctx, TurnBasedActivity game, int minX, int minY, int maxX, int maxY) {
        super(name, x, y, icon, movespeed, Owners.HARD, graph, board, ctx, game, minX, minY, maxX, maxY);
    }
}
