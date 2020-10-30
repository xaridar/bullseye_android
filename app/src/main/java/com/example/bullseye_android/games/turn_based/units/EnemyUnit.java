package com.example.bullseye_android.games.turn_based.units;

import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Tile;

public class EnemyUnit extends Unit {
    public EnemyUnit(String name, int x, int y, String icon, int movespeed, Owners owner, Tile[][] board) {
        super(name, x, y, icon, movespeed, owner, board);
    }
}
