package com.example.bullseye_android.games.turn_based;

import com.example.bullseye_android.games.turn_based.units.Unit;

import java.util.ArrayList;

public interface MoveableUnit {

    void update();
    void movement(Tile[][] map, Node[][] graph);
    void onKillUnit(Unit killedUnit, Tile[][] map, Node[][] graph);
    void onMovementFinished(Tile[][] map, Node[][] graph);
    void onGoalReached(Tile[][] map, Node[][] graph);

}
