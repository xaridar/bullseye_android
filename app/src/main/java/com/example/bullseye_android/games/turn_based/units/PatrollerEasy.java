package com.example.bullseye_android.games.turn_based.units;

import android.util.Pair;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Tile;
import com.example.bullseye_android.games.turn_based.TurnBasedActivity;

import java.util.ArrayList;

public class PatrollerEasy extends Patroller {

    public PatrollerEasy(String name, int x, int y, String icon, int movespeed,  Node[][] graph, Tile[][] board, LifecycleOwner ctx, TurnBasedActivity game, ArrayList<Pair<Integer, Integer>> patrolPoints) {
        super(name, x, y, icon, movespeed, Owners.EASY, graph, board, ctx, game, patrolPoints);
    }
}
