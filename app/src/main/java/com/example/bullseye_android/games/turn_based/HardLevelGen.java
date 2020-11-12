package com.example.bullseye_android.games.turn_based;

import android.util.Pair;

import com.example.bullseye_android.games.turn_based.units.Follower;
import com.example.bullseye_android.games.turn_based.units.Patroller;
import com.example.bullseye_android.games.turn_based.units.PatrollerHard;
import com.example.bullseye_android.games.turn_based.units.Unit;
import com.example.bullseye_android.games.turn_based.units.Wanderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HardLevelGen implements LevelGen {

    @Override
    public List<Patroller> genPatrollers(Tile[][] board, Node[][] graph, TurnBasedActivity game) {
        ArrayList<Patroller> units = new ArrayList<>();
        Random random = new Random();
        int set = random.nextInt(3);
        switch(set) {
            case 0:
                units.add(new PatrollerHard("patroller1", 0, 1, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(4, 1)))));
                units.add(new PatrollerHard("patroller2", 4, 2, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(0, 2)))));
                units.add(new PatrollerHard("patroller3", 0, 3, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(4, 3)))));
                units.add(new PatrollerHard("patroller4", 4, 4, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(0, 4)))));
                break;
            case 1:
                units.add(new PatrollerHard("patroller1", 0, 5, "ic_strat_img_duck", 1, graph, board, game, game,
                        new ArrayList<>(Arrays.asList(
                                new Pair<>(4, 5),
                                new Pair<>(4, 2),
                                new Pair<>(0, 2)
                        ))));
                units.add(new PatrollerHard("patroller1", 3, 3, "ic_strat_img_duck", 1, graph, board, game, game,
                        new ArrayList<>(Arrays.asList(
                                new Pair<>(3, 4),
                                new Pair<>(1, 4),
                                new Pair<>(1, 3)
                        ))));
                break;
            case 2:
                units.add(new PatrollerHard("patroller1", 2, 3, "ic_strat_img_duck", 1, graph, board, game, game,
                        new ArrayList<>(Arrays.asList(
                                new Pair<>(2, 4)
                        ))));
                units.add(new PatrollerHard("patroller1", 1, 2, "ic_strat_img_duck", 1, graph, board, game, game,
                        new ArrayList<>(Arrays.asList(
                                new Pair<>(3, 2),
                                new Pair<>(3, 5),
                                new Pair<>(1, 5)
                        ))));
                units.add(new PatrollerHard("patroller1", 0, 5, "ic_strat_img_duck", 1, graph, board, game, game,
                        new ArrayList<>(Arrays.asList(
                                new Pair<>(0, 2)
                        ))));
                units.add(new PatrollerHard("patroller1", 4, 5, "ic_strat_img_duck", 1, graph, board, game, game,
                        new ArrayList<>(Arrays.asList(
                                new Pair<>(4, 2)
                        ))));
                break;
            case 3:
                units.add(new PatrollerHard("patroller1", 0, 1, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(4, 1)))));
                units.add(new PatrollerHard("patroller2", 0, 2, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(4, 2)))));
                units.add(new PatrollerHard("patroller3", 0, 3, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(4, 3)))));
                units.add(new PatrollerHard("patroller4", 0, 4, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(5, 4)))));
                units.add(new PatrollerHard("patroller4", 0, 5, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(5, 5)))));
                break;
        }


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

        Random random = new Random();
        int set = random.nextInt(3);
        switch(set) {
            case 0:
                units.add(new Follower("follower", 2, 0, "ic_strat_img_snake", 2, Owners.HARD, graph, board, game, game));
                break;
            case 1:
                units.add(new Follower("follower", 1, 0, "ic_strat_img_snake", 1, Owners.HARD, graph, board, game, game));
                units.add(new Follower("follower", 3, 0, "ic_strat_img_snake", 1, Owners.HARD, graph, board, game, game));
                break;
            case 2:
                units.add(new Follower("follower", 0, 0, "ic_strat_img_snake", 2, Owners.HARD, graph, board, game, game));
                units.add(new Follower("follower", 4, 0, "ic_strat_img_snake", 2, Owners.HARD, graph, board, game, game));
                break;
        }
        
        return units;
    }
}
