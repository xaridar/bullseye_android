package com.example.bullseye_android.games.turn_based.units;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Tile;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class EnemyUnit extends Unit {
    public EnemyUnit(String name, int x, int y, String icon, int movespeed, Owners owner, Tile[][] board, LifecycleOwner ctx) {
        super(name, x, y, icon, movespeed, owner, board, ctx);
    }

    @Override
    public void onMovementFinished(Tile[][] map, Node[][] graph) {
        currentPath.add(currentPath.remove(0));
    }
}
