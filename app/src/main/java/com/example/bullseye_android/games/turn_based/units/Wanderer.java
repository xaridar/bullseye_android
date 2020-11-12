package com.example.bullseye_android.games.turn_based.units;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Pathfinder;
import com.example.bullseye_android.games.turn_based.Tile;
import com.example.bullseye_android.games.turn_based.TurnBasedActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Wanderer extends EnemyUnit {

    int minX;
    int minY;
    int maxX;
    int maxY;

    int goalX;
    int goalY;

    Random random;

    Node[][] graph;

    public Wanderer(String name, int x, int y, String icon, int movespeed, Owners owner, Node[][] graph, Tile[][] board, LifecycleOwner ctx, TurnBasedActivity game, int minX, int minY, int maxX, int maxY){
        super(name, x, y, icon, movespeed, Owners.EASY, board, graph, ctx, game);
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.board = board;
        this.graph = graph;
        random = new Random();
        generateRandomPoint(graph, board);
        setCurrentPath(Pathfinder.generatePathTo(x, y, goalX, goalY, graph, board, this));
        if(Pathfinder.generatePathTo(x, y, goalX, goalY, graph, board, this).size() <= 0){
            generateRandomPoint(graph, board);
        }
    }

    @Override
    public void onMovementFinished(Tile[][] map, Node[][] graph) {
        currentPath.remove(0);
        if(!Pathfinder.unitCanReachPoint(goalX, goalY, board, graph, this)) {
            generateRandomPoint(graph, map);
            setCurrentPath(Pathfinder.generatePathTo(x, y, goalX, goalY, graph, map, this));
        }
        if(currentPath.size() > 1 && !Pathfinder.unitCanEnterTile(currentPath.get(1).x, currentPath.get(1).y, map, this)){
            generateRandomPoint(graph, map);
            setCurrentPath(Pathfinder.generatePathTo(x, y, goalX, goalY, graph, map, this));
        }
    }

    @Override
    public void onGoalReached(Tile[][] map, Node[][] graph) {
        super.onGoalReached(map, graph);
        generateRandomPoint(graph, map);
        setCurrentPath(Pathfinder.generatePathTo(x, y, goalX, goalY, graph, map, this));
        if(Pathfinder.generatePathTo(x, y, goalX, goalY, graph, map, this).size() <= 0){
            generateRandomPoint(graph, map);
            setCurrentPath(Pathfinder.generatePathTo(x, y, goalX, goalY, graph, map, this));
        }
    }

    @Override
    public void onMovementInterrupted(Tile[][] map, Node[][] graph) {
        generateRandomPoint(graph, map);
        setCurrentPath(Pathfinder.generatePathTo(x, y, goalX, goalY, graph, map, this));
    }



    public void generateRandomPoint(Node[][] graph, Tile[][] map){
        int randX = -1;
        int randY = -1;
        do {
            Log.i("TB", randX + ", " + randY);
            randX = random.nextInt(maxX - minX + 1) + minX;
            randY = random.nextInt(maxY - minY + 1) + minY;
        }while((randX == -1 && randY == -1) || (randX == x && randY == y) || (!Pathfinder.unitCanReachPoint(randX, randY, map, graph, this)));

        goalX = randX;
        goalY = randY;
        Log.i("TB","Goal: " + goalX + ", " + goalY);
    }

    @Override
    public void update() {
        super.update();

    }
}
