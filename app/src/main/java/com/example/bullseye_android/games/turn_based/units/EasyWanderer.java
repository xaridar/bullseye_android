package com.example.bullseye_android.games.turn_based.units;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Pathfinder;
import com.example.bullseye_android.games.turn_based.Tile;

import java.util.ArrayList;
import java.util.Random;

public class EasyWanderer extends EnemyUnit {

    int minX;
    int minY;
    int maxX;
    int maxY;

    Random random = new Random();

    public EasyWanderer(String name, int x, int y, String icon, int movespeed, Node[][] graph, Tile[][] board, LifecycleOwner ctx, int minX, int minY, int maxX, int maxY){
        super(name, x, y, icon, movespeed, Owners.EASY, board, ctx);
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        setCurrentPath(generateRandomPoint(graph, board));
    }

    @Override
    public void onMovementFinished(Tile[][] map, Node[][] graph) {
        currentPath.remove(0);
    }

    @Override
    public void onPathfindingEnd(Tile[][] map, Node[][] graph) {
        super.onPathfindingEnd(map, graph);
        setCurrentPath(generateRandomPoint(graph, map));
    }

    public ArrayList<Node> generateRandomPoint(Node[][] graph, Tile[][] map){
        int randX = -1;
        int randY = -1;
        do {
            randX = random.nextInt(maxX - minX + 1) + minX;
            randY = random.nextInt(maxY - minY + 1) + minY;
        }while((randX == -1 && randY == -1) || map[randX][randY].getUnit() != null);

        return(Pathfinder.generatePathTo(x, y, randX, randY, graph, map, this));
    }
}
