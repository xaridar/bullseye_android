package com.example.bullseye_android.games.turn_based;

import com.example.bullseye_android.games.turn_based.units.Follower;
import com.example.bullseye_android.games.turn_based.units.Patroller;
import com.example.bullseye_android.games.turn_based.units.Unit;
import com.example.bullseye_android.games.turn_based.units.Wanderer;

import java.util.ArrayList;
import java.util.List;

public interface LevelGen {

    public default ArrayList<Unit> genLevel(Tile[][] board, Node[][] graph, TurnBasedActivity game){
        ArrayList<Unit> units = new ArrayList<>();

        units.addAll(genPatrollers(board, graph, game));
        units.addAll(genWanderers(board, graph, game));
        units.addAll(genFollowers(board, graph, game));

        return units;
    }

    List<Patroller> genPatrollers(Tile[][] board, Node[][] graph, TurnBasedActivity game);

    List<Wanderer> genWanderers(Tile[][] board, Node[][] graph, TurnBasedActivity game);

    List<Follower> genFollowers(Tile[][] board, Node[][] graph, TurnBasedActivity game);

}
