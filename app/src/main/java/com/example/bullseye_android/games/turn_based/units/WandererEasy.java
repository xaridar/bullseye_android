package com.example.bullseye_android.games.turn_based.units;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Tile;

public class WandererEasy extends Wanderer {
    public WandererEasy(String name, int x, int y, String icon, int movespeed, Node[][] graph, Tile[][] board, LifecycleOwner ctx, int minX, int minY, int maxX, int maxY) {
        super(name, x, y, icon, movespeed, Owners.EASY, graph, board, ctx, minX, minY, maxX, maxY);
        showNextLocation(board);
    }

    @Override
    public void onMovementFinished(Tile[][] map, Node[][] graph) {
        super.onMovementFinished(map, graph);
        showNextLocation(map);
    }

    @Override
    public void onGoalReached(Tile[][] map, Node[][] graph) {
        super.onGoalReached(map, graph);
        showNextLocation(map);
    }

    private void showNextLocation(Tile[][] map){
        for(Tile[] tiles : map){
            for(Tile tile : tiles){
                tile.setPadding(0,0,0,0);
                tile.setIcon("");
            }
        }
        if(currentPath.size() > 1) {
            String direction = "";
//            COMING FROM TOP:
            if(currentPath.get(1).y-1 >= 0) {
                if (map[currentPath.get(1).x][currentPath.get(1).y - 1] == map[currentPath.get(0).x][currentPath.get(0).y]) {
                    map[currentPath.get(1).x][currentPath.get(1).y].setPadding(0, 0, 0, 200);
                    direction = "top";
                }
            }
//            COMING FROM BOTTOM:
            if(currentPath.get(1).y+1 < map[currentPath.get(1).x].length) {
                if (map[currentPath.get(1).x][currentPath.get(1).y + 1] == map[currentPath.get(0).x][currentPath.get(0).y]) {
                    map[currentPath.get(1).x][currentPath.get(1).y].setPadding(0, 200, 0, 0);
                    direction = "bottom";
                }
            }
//            COMING FROM LEFT
            if(currentPath.get(1).x-1 >= 0) {
                if (map[currentPath.get(1).x - 1][currentPath.get(1).y] == map[currentPath.get(0).x][currentPath.get(0).y]) {
                    map[currentPath.get(1).x][currentPath.get(1).y].setPadding(0, 0, 150, 0);
                    direction = "left";
                }
            }
//            COMING FROM RIGHT:
            if(currentPath.get(1).x+1 < map.length) {
                if (map[currentPath.get(1).x + 1][currentPath.get(1).y] == map[currentPath.get(0).x][currentPath.get(0).y]) {
                    map[currentPath.get(1).x][currentPath.get(1).y].setPadding(150, 0, 0, 0);
                    direction = "right";
                }
            }
            map[currentPath.get(1).x][currentPath.get(1).y].setIcon("ic_strat_img_" + direction);
        }
    }
}
