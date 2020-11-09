package com.example.bullseye_android.games.turn_based.units;

import android.util.Pair;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Tile;
import com.example.bullseye_android.games.turn_based.TurnBasedActivity;

import java.util.ArrayList;

public class PatrollerHard extends Patroller {

    public PatrollerHard(String name, int x, int y, String icon, int movespeed, Node[][] graph, Tile[][] board, LifecycleOwner ctx, TurnBasedActivity game, ArrayList<Pair<Integer, Integer>> patrolPoints) {
        super(name, x, y, icon, movespeed, Owners.HARD, graph, board, ctx, game, patrolPoints);
    }

    @Override
    protected ArrayList<Node> onPatrolPathFinish(ArrayList<Node> currentPath) {
        for(int i=0; i < currentPath.size(); i++){
            Node lastPoint = null;
            lastPoint = i > 0 ? currentPath.get(i-1) : currentPath.get(currentPath.size()-1);
            if(currentPath.get(i) == lastPoint) currentPath.remove(i > 0 ? i-1 : currentPath.size()-1);
        }
        return currentPath;
    }
}
