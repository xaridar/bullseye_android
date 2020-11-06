package com.example.bullseye_android.games.turn_based.units;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Pathfinder;
import com.example.bullseye_android.games.turn_based.Tile;

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

    Tile[][] board;
    Node[][] graph;
    boolean updatedBoundingBox = false;

    public Wanderer(String name, int x, int y, String icon, int movespeed, Owners owner, Node[][] graph, Tile[][] board, LifecycleOwner ctx, int minX, int minY, int maxX, int maxY){
        super(name, x, y, icon, movespeed, Owners.EASY, board, ctx);
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.board = board;
        this.graph = graph;
        random = new Random();
        generateRandomPoint(graph, board);
    }

    @Override
    public void onMovementFinished(Tile[][] map, Node[][] graph) {
        currentPath.remove(0);
//        if(!Pathfinder.unitCanEnterTile(goalX, goalY, map, this)) {
//            Log.i("TB",name + ": goal has a unit: " + board[goalX][goalY].getUnit().getName());
//            setCurrentPath(generateRandomPoint(graph, board));
//            Log.i("TB", name + ": " + goalX + ", " + goalY);
//        }
    }

    @Override
    public void onGoalReached(Tile[][] map, Node[][] graph) {
        super.onGoalReached(map, graph);
        generateRandomPoint(graph, map);
    }

    public void generateRandomPoint(Node[][] graph, Tile[][] map){
        int randX = -1;
        int randY = -1;
        do {
            randX = random.nextInt(maxX - minX + 1) + minX;
            randY = random.nextInt(maxY - minY + 1) + minY;
        }while((randX == -1 && randY == -1) || (randX == x && randY == y) || (!Pathfinder.unitCanEnterTile(randX, randY, map, this)));

        goalX = randX;
        goalY = randY;
    }

    @Override
    public void update() {
        super.update();
        setCurrentPath(Pathfinder.generatePathTo(x, y, goalX, goalY, graph, board, this));
        if(Pathfinder.generatePathTo(x, y, goalX, goalY, graph, board, this).size() <= 0){
            generateRandomPoint(graph, board);
        }
        if(!updatedBoundingBox) {
            int totalUnits = (int) Arrays.stream(board).flatMap(Arrays::stream).filter(tile -> tile.getUnit() != null && tile.getUnit().getOwner() != Owners.PLAYER).count();
            if (totalUnits == 0) {
                Log.i("TB", "updating bounding box");
                updatedBoundingBox = true;
                minX = 0;
                minY = 0;
                maxX = board.length-1;
                maxY = board[0].length-1;
            }
        }
    }
}
