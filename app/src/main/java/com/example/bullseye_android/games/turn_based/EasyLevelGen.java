package com.example.bullseye_android.games.turn_based;

import android.util.Pair;

import com.example.bullseye_android.games.turn_based.units.Follower;
import com.example.bullseye_android.games.turn_based.units.Patroller;
import com.example.bullseye_android.games.turn_based.units.PatrollerEasy;
import com.example.bullseye_android.games.turn_based.units.Unit;
import com.example.bullseye_android.games.turn_based.units.Wanderer;
import com.example.bullseye_android.games.turn_based.units.WandererEasy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EasyLevelGen implements LevelGen{

    @Override
    public List<Patroller> genPatrollers(Tile[][] board, Node[][] graph, TurnBasedActivity game) {
        ArrayList<Patroller> patrollers = new ArrayList<>();

        for(int i=0; i<board.length;i++){
            patrollers.add(new PatrollerEasy("patroller" + (i+1), i, 2, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(i, 4)))));
        }

        return patrollers;
    }

    @Override
    public List<Wanderer> genWanderers(Tile[][] board, Node[][] graph, TurnBasedActivity game) {
        ArrayList<Wanderer> wanderers = new ArrayList<>();

        wanderers.add(new WandererEasy("wanderer", 0, 0, "ic_strat_img_frog", 2, graph, board, game, game, 0, 0, 4,6));

        return wanderers;
    }

    @Override
    public List<Follower> genFollowers(Tile[][] board, Node[][] graph, TurnBasedActivity game) {
        return null;
    }

}
