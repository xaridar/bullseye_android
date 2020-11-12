package com.example.bullseye_android.games.turn_based;

import android.util.Pair;

import com.example.bullseye_android.games.turn_based.units.Follower;
import com.example.bullseye_android.games.turn_based.units.Patroller;
import com.example.bullseye_android.games.turn_based.units.PatrollerEasy;
import com.example.bullseye_android.games.turn_based.units.Unit;
import com.example.bullseye_android.games.turn_based.units.Wanderer;
import com.example.bullseye_android.games.turn_based.units.WandererEasy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EasyLevelGen implements LevelGen{

    @Override
    public List<Patroller> genPatrollers(Tile[][] board, Node[][] graph, TurnBasedActivity game) {
        ArrayList<Patroller> patrollers = new ArrayList<>();
        Random random = new Random();
        int set = random.nextInt(3);
        switch(set){
            case 0:
                for(int i=0; i<board.length;i++){
                    patrollers.add(new PatrollerEasy("patroller" + (i+1), i, 2, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(i, 4)))));
                }
                break;
            case 1:
                for(int i=0; i<board.length;i++){
                    patrollers.add(new PatrollerEasy("patroller" + (i+1), i, 4, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(i, 2)))));
                }
                break;
            case 2:
                patrollers.add(new PatrollerEasy("patroller", 0, 4, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(0, 2)))));
                patrollers.add(new PatrollerEasy("patroller", 1, 2, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(1, 4)))));
                patrollers.add(new PatrollerEasy("patroller", 2, 4, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(2, 2)))));
                patrollers.add(new PatrollerEasy("patroller", 3, 2, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(3, 4)))));
                patrollers.add(new PatrollerEasy("patroller", 4, 4, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Collections.singletonList(new Pair<>(4, 2)))));
                break;
            case 3:
                patrollers.add(new PatrollerEasy("patroller", 0, 5, "ic_strat_img_duck", 1, graph, board, game, game, new ArrayList<>(Arrays.asList(new Pair<>(4, 5), new Pair<>(2, 2)))));
                break;
        }


        return patrollers;
    }

    @Override
    public List<Wanderer> genWanderers(Tile[][] board, Node[][] graph, TurnBasedActivity game) {
        ArrayList<Wanderer> wanderers = new ArrayList<>();

        Random random = new Random();
        int set = random.nextInt(4);
        switch(set) {
            case 0:
                wanderers.add(new WandererEasy("wanderer", 0, 0, "ic_strat_img_frog", 2, graph, board, game, game, 0, 0, 4,6));
                break;
            case 1:
                wanderers.add(new WandererEasy("wanderer", 0, 0, "ic_strat_img_frog", 2, graph, board, game, game, 0, 0, 4,6));
                wanderers.add(new WandererEasy("wanderer", 4, 0, "ic_strat_img_frog", 2, graph, board, game, game, 0, 0, 4,6));
                break;
            case 2:
                wanderers.add(new WandererEasy("wanderer", 2, 4, "ic_strat_img_frog", 2, graph, board, game, game, 0, 0, 4,6));
                break;
            case 3:
                wanderers.add(new WandererEasy("wanderer", 4, 0, "ic_strat_img_frog", 2, graph, board, game, game, 0, 0, 4,6));
                break;
            case 4:
                wanderers.add(new WandererEasy("wanderer", 2, 0, "ic_strat_img_frog", 2, graph, board, game, game, 0, 0, 4,1));
        }



        return wanderers;
    }

    @Override
    public List<Follower> genFollowers(Tile[][] board, Node[][] graph, TurnBasedActivity game) {
        return new ArrayList<>();
    }

}
