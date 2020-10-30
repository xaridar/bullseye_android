package com.example.bullseye_android.games.turn_based;

public interface MoveableUnit {

    void update();
    void movement(Tile[][] map, Node[][] graph);

}
