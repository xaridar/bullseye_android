package com.example.bullseye_android.games.turn_based.units;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.bullseye_android.games.turn_based.BoardActor;
import com.example.bullseye_android.games.turn_based.MoveableUnit;
import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Pathfinder;
import com.example.bullseye_android.games.turn_based.Tile;
import com.example.bullseye_android.games.turn_based.TurnBasedActivity;
import com.example.bullseye_android.music.MusicActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Unit extends BoardActor implements MoveableUnit, MusicActivity {

    ArrayList<Node> currentPath = null;

    int movespeed;

    private Owners owner;

    Tile[][] board;
    Node[][] graph;

    Timer timer;

    TurnBasedActivity game;

    private boolean moved = false;
    MutableLiveData<Boolean> dead = new MutableLiveData<>();
    private boolean justDied = false;

    public Unit(String name, int x, int y, String icon, int movespeed, Owners owner, Tile[][] board, Node[][] graph, LifecycleOwner ctx, TurnBasedActivity game){
        this.name = name;
        this.x = x;
        this.y = y;
        this.icon = icon;
        this.movespeed = movespeed;
        this.owner = owner;
        this.game = game;
        this.board = board;
        this.graph = graph;
        dead.setValue(false);

        dead.observe(ctx, isDead ->{
            if(isDead){
               try{
                   timer.cancel();
               }catch (NullPointerException ignored){

               }
            }
        });

        board[x][y].setUnit(this);
    }

    @Override
    public void update() {
        board[x][y].setUnit(currentPath != null && (currentPath.size() > 0 && ((currentPath.get(0).x != x) && (currentPath.get(0).y != y))) ? null : this);
        board = game.getBoard();
    }

    @Override
    public void movement(Tile[][] map, Node[][] graph) {

        final float[] remainingMovement = {(float) movespeed};
        int moveTime;
        if(currentPath != null && currentPath.size() > 1) {
            moveTime = 600 / Math.min(movespeed, currentPath.size() - 1);
        }else{
            moveTime = 600;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(remainingMovement[0] <= 1) {
                    cancel();
                }

                if(currentPath == null){
                    return;
                }
                Tile oldTile;
                Tile newTile;
                if(currentPath.size() > 1) {
                    oldTile = map[currentPath.get(0).x][currentPath.get(0).y];
                    newTile = map[currentPath.get(1).x][currentPath.get(1).y];
                    if(Pathfinder.unitCanEnterTile(newTile.x, newTile.y, board, Unit.this)) {
                        if (newTile.getUnit() != null && (newTile.getUnit() != Unit.this)) {
                            onKillUnit(newTile.getUnit(), map, graph);
                        }
                        remainingMovement[0] -= map[currentPath.get(1).x][currentPath.get(1).y].getCost();
                        oldTile.setUnit(null);
                        newTile.setUnit(Unit.this);
                        Unit.this.x = newTile.x;
                        Unit.this.y = newTile.y;

                        if (currentPath.size() <= 1) {
                            onGoalReached(map, graph);
                        }else{
                            onMovementFinished(map, graph);
                        }
                    }else{
                        onMovementInterrupted(map, graph);
                    }
                }

            }
        }, moveTime, moveTime);
    }

    @Override
    public void onKillUnit(Unit killedUnit, Tile[][] map, Node[][] graph) {
        killedUnit.setJustDied(true);
    }

    @Override
    public void onMovementFinished(Tile[][] map, Node[][] graph) {
        currentPath.remove(0);
    }

    @Override
    public void onGoalReached(Tile[][] map, Node[][] graph) {
        currentPath = null;
    }

    @Override
    public void onMovementInterrupted(Tile[][] map, Node[][] graph) {

    }

    public ArrayList<Node> getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(ArrayList<Node> currentPath) {
        this.currentPath = currentPath;
    }

    public int getMovespeed() {
        return movespeed;
    }

    public void setMovespeed(int movespeed) {
        this.movespeed = movespeed;
    }

    public Owners getOwner() {
        return owner;
    }

    public void setOwner(Owners owner) {
        this.owner = owner;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isDead() {
        return dead.getValue();
    }

    public void setDead(boolean dead) {
        this.dead.setValue(dead);
    }

    public boolean isJustDied() {
        return justDied;
    }

    public void setJustDied(boolean justDied) {
        this.justDied = justDied;
    }

    public Timer getTimer(){ return timer; }

    public void setTimer(Timer timer){ this.timer = timer; }
}
