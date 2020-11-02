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

public class EasyPatroller extends EnemyUnit {

    ArrayList<Pair<Integer, Integer>> patrolPoints = new ArrayList<>();
    ArrayList<Node> currentPath = new ArrayList<>();
    int movespeed;
    Timer movementTimer;

    public EasyPatroller(String name, int x, int y, String icon, int movespeed, Node[][] graph, Tile[][] board, LifecycleOwner ctx, ArrayList<Pair<Integer, Integer>> patrolPoints){
        super(name, x, y, icon, movespeed, Owners.EASY, board, ctx);
        this.patrolPoints.add(new Pair<>(x,y));
        this.patrolPoints.addAll(patrolPoints);
        this.movespeed = movespeed;

        for(int i=0;i<this.patrolPoints.size();i++){
            int patrolTarget = i+1;
            if(i==this.patrolPoints.size()-1) {
                patrolTarget = 0;
            }
            currentPath.addAll(Pathfinder.generatePathTo(this.patrolPoints.get(i).first, this.patrolPoints.get(i).second, this.patrolPoints.get(patrolTarget).first, this.patrolPoints.get(patrolTarget).second, graph, board, this));
        }
        setCurrentPath(currentPath);
    }

    public void addPatrolPoint(Pair<Integer, Integer> point, Node[][] graph, Tile[][] board){
        patrolPoints.add(point);
        currentPath.addAll(Pathfinder.generatePathTo(getX(), getY(), patrolPoints.get(0).first, patrolPoints.get(0).second, graph, board, this));
        for(int i=0;i<patrolPoints.size();i++){
            int patrolTarget = i;
            if(i==patrolPoints.size()-1) {
                patrolTarget = 0;
            }
            currentPath.addAll(Pathfinder.generatePathTo(patrolPoints.get(i).first, patrolPoints.get(i).second, patrolPoints.get(patrolTarget).first, patrolPoints.get(patrolTarget).second, graph, board, this));
        }
    }

    public void addPatrolPoint(Pair<Integer, Integer> point, int index, Node[][] graph, Tile[][] board){
        patrolPoints.add(index, point);
        currentPath.addAll(Pathfinder.generatePathTo(getX(), getY(), patrolPoints.get(0).first, patrolPoints.get(0).second, graph, board, this));
        for(int i=0;i<patrolPoints.size();i++){
            int patrolTarget = i;
            if(i==patrolPoints.size()-1) {
                patrolTarget = 0;
            }
            currentPath.addAll(Pathfinder.generatePathTo(patrolPoints.get(i).first, patrolPoints.get(i).second, patrolPoints.get(patrolTarget).first, patrolPoints.get(patrolTarget).second, graph, board, this));
        }
    }

}