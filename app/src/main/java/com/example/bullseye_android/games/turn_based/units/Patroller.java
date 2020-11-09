package com.example.bullseye_android.games.turn_based.units;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.bullseye_android.games.turn_based.MoveableUnit;
import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Pathfinder;
import com.example.bullseye_android.games.turn_based.Tile;

import java.util.ArrayList;
import java.util.Timer;

public class Patroller extends EnemyUnit {

    ArrayList<Pair<Integer, Integer>> patrolPoints = new ArrayList<>();
    ArrayList<Node> path = new ArrayList<Node>();

    Tile[][] board;

    public Patroller(String name, int x, int y, String icon, int movespeed, Owners owner, Node[][] graph, Tile[][] board, LifecycleOwner ctx, ArrayList<Pair<Integer, Integer>> patrolPoints){
        super(name, x, y, icon, movespeed, owner, board, ctx);
        this.patrolPoints.add(new Pair<>(x,y));
        this.patrolPoints.addAll(patrolPoints);
        this.movespeed = movespeed;
        this.board = board;
        currentPath = new ArrayList<>();

        for(int i=0;i<this.patrolPoints.size();i++){
            int patrolTarget = i+1;
            if(i==this.patrolPoints.size()-1) patrolTarget = 0;
            currentPath.addAll(Pathfinder.generatePathTo(this.patrolPoints.get(i).first, this.patrolPoints.get(i).second, this.patrolPoints.get(patrolTarget).first, this.patrolPoints.get(patrolTarget).second, graph, board, this));
        }
        currentPath = onPatrolPathFinish(currentPath);
        setCurrentPath(currentPath);
    }

    protected ArrayList<Node> onPatrolPathFinish(ArrayList<Node> currentPath) {
        return currentPath;
    }

    @Override
    public void onMovementFinished(Tile[][] map, Node[][] graph) {
        currentPath.add(currentPath.remove(0));
    }

    @Override
    public void update() {
        super.update();
        if(currentPath != null) path = currentPath;
        currentPath = (currentPath != null && (currentPath.size() > 1 && !Pathfinder.unitCanEnterTile(currentPath.get(1).x, currentPath.get(1).y, board, this))) ? null : path;
        if(currentPath == null) Log.i("TB", name + ": current path is null");
    }
}