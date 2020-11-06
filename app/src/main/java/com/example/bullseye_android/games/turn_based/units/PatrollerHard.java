package com.example.bullseye_android.games.turn_based.units;

import android.util.Pair;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Tile;

import java.util.ArrayList;

public class PatrollerHard extends Patroller {
    public PatrollerHard(String name, int x, int y, String icon, int movespeed, Node[][] graph, Tile[][] board, LifecycleOwner ctx, ArrayList<Pair<Integer, Integer>> patrolPoints) {
        super(name, x, y, icon, movespeed, Owners.HARD, graph, board, ctx, patrolPoints);
    }

    @Override
    protected ArrayList<Node> onPatrolPathFinish(ArrayList<Node> currentPath) {
        for(int i=0; i < currentPath.size(); i++){
            Node lastPoint = null;
            if(i > 0){
                lastPoint = currentPath.get(i-1);
            }else{
                lastPoint = currentPath.get(currentPath.size()-1);
            }
            if(currentPath.get(i) == lastPoint){
                if(i > 0){
                    currentPath.remove(i-1);
                }else{
                    currentPath.remove(currentPath.size()-1);
                }

            }
        }
        return currentPath;
    }
}
