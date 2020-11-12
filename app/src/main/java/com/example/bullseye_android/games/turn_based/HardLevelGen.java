package com.example.bullseye_android.games.turn_based;

import android.util.Pair;

import com.example.bullseye_android.games.turn_based.units.Follower;
import com.example.bullseye_android.games.turn_based.units.Patroller;
import com.example.bullseye_android.games.turn_based.units.PatrollerHard;
import com.example.bullseye_android.games.turn_based.units.Unit;
import com.example.bullseye_android.games.turn_based.units.Wanderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HardLevelGen implements LevelGen {

    @Override
    public List<Patroller> genPatrollers(Tile[][] board, Node[][] graph, TurnBasedActivity game) {
        ArrayList<Patroller> units = new ArrayList<>();

        units.add(new PatrollerHard("patroller1",0, 1, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(4, 1)))));
        units.add(new PatrollerHard("patroller2",4, 2, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(0, 2)))));
        units.add(new PatrollerHard("patroller3",0, 3, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(4, 3)))));
        units.add(new PatrollerHard("patroller4",4, 4, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(0, 4)))));

        return units;
    }

    @Override
    public List<Wanderer> genWanderers(Tile[][] board, Node[][] graph, TurnBasedActivity game) {
        ArrayList<Wanderer> units = new ArrayList<>();
        return units;
    }

    @Override
    public List<Follower> genFollowers(Tile[][] board, Node[][] graph, TurnBasedActivity game) {
        ArrayList<Follower> units = new ArrayList<>();

        units.add(new Follower("follower", 2, 0, "ic_strat_img_snake", 2, Owners.HARD, graph, board, game, game));

        return units;
    }
}
