package com.example.bullseye_android.games.turn_based.units;

import android.util.Log;
import android.util.Pair;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Pathfinder;
import com.example.bullseye_android.games.turn_based.Tile;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class EasyPatroller extends Unit {

    ArrayList<Pair<Integer, Integer>> patrolPoints = new ArrayList<>();
    ArrayList<Node> currentPath = new ArrayList<Node>();
    int movespeed;

    public EasyPatroller(String name, int x, int y, ArrayList<Integer> tags, String icon, int movespeed, ArrayList<Pair<Integer, Integer>> patrolPoints, Node[][] graph, Tile[][] board){
        super(name, x, y, tags, icon, movespeed, Owners.EASY, board);
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
        board[x][y].setUnit(this);
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

    @Override
    public void movement(Tile[][] map, Node[][] graph) {
        final float[] remainingMovement = {(float) movespeed};
        int moveTime;
        if(currentPath != null && currentPath.size() > 0) {
            moveTime = 600 / Math.min(movespeed, currentPath.size() - 1);
        }else{
            moveTime = 600;
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                if(remainingMovement[0] <= 1) {
                    cancel();
                }

                if(currentPath == null){
                    return;
                }

                Tile oldTile = map[currentPath.get(0).getX()][currentPath.get(0).getY()];
                Tile newTile = map[currentPath.get(1).getX()][currentPath.get(1).getY()];

                if(newTile.getUnit() == null) {
                    Log.i("tbdubug",currentPath.toString() + ", no unit");
                    remainingMovement[0] -= map[currentPath.get(1).x][currentPath.get(1).y].getCost();
                    oldTile.setUnit(null);
                    newTile.setUnit(EasyPatroller.this);

                    for (int x = 0; x < map.length; x++) {
                        for (int y = 0; y < map[x].length; y++) {
                            if (map[x][y].getUnit() == EasyPatroller.this) {
                                EasyPatroller.this.x = x;
                                EasyPatroller.this.y = y;
                            }
                        }
                    }
                    currentPath.add(currentPath.remove(0));
                }else{
                    if((newTile.getUnit().getOwner() != EasyPatroller.this.getOwner()) || newTile.getUnit() == EasyPatroller.this){
                        Log.i("tbdubug",currentPath.toString() + ", unit isnt same owner");
                        if(newTile.getUnit() != EasyPatroller.this){
                            Unit killedUnit = newTile.getUnit();
                            killedUnit.setDead(true);
                            killedUnit.setJustDied(true);
                        }

                        remainingMovement[0] -= map[currentPath.get(1).x][currentPath.get(1).y].getCost();
                        oldTile.setUnit(null);
                        newTile.setUnit(EasyPatroller.this);

                        for (int x = 0; x < map.length; x++) {
                            for (int y = 0; y < map[x].length; y++) {
                                if (map[x][y].getUnit() == EasyPatroller.this) {
                                    EasyPatroller.this.x = x;
                                    EasyPatroller.this.y = y;
                                }
                            }
                        }
                        currentPath.add(currentPath.remove(0));;
                    }
                }



                if(currentPath.size() == 1){
                    currentPath = null;
                }
            }
        }, moveTime, moveTime);
    }
}