package com.example.bullseye_android.games.turn_based.units;

import com.example.bullseye_android.games.turn_based.BoardActor;
import com.example.bullseye_android.games.turn_based.MoveableUnit;
import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Tile;
import com.example.bullseye_android.music.MusicActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Unit extends BoardActor implements MoveableUnit, MusicActivity {

    private ArrayList<Node> currentPath = null;

    private int movespeed;

    private Owners owner;

    private Timer timer = new Timer();

    private boolean moved = false;
    private boolean dead = false;
    private boolean justDied = false;

    public Unit(String name, int x, int y, String icon, int movespeed, Owners owner, Tile[][] board){
        this.name = name;
        this.x = x;
        this.y = y;
        this.icon = icon;
        this.movespeed = movespeed;
        this.owner = owner;
        board[x][y].setUnit(this);
    }

    @Override
    public void update() {

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

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(remainingMovement[0] <= 1) {
                    cancel();
                }

                if(currentPath == null){
                    return;
                }



                Tile oldTile = map[currentPath.get(0).x][currentPath.get(0).y];
                Tile newTile = map[currentPath.get(1).x][currentPath.get(1).y];
                if(newTile.getUnit() == null) {
                    remainingMovement[0] -= map[currentPath.get(1).x][currentPath.get(1).y].getCost();
                    oldTile.setUnit(null);
                    newTile.setUnit(Unit.this);

                    for (int x = 0; x < map.length; x++) {
                        for (int y = 0; y < map[x].length; y++) {
                            if (map[x][y].getUnit() == Unit.this) {
                                Unit.this.x = x;
                                Unit.this.y = y;
                            }
                        }
                    }
                    currentPath.remove(0);
                }else{
                    if((newTile.getUnit().getOwner() != Unit.this.getOwner()) || newTile.getUnit() == Unit.this){
                        if(newTile.getUnit() != Unit.this){
                            Unit killedUnit = newTile.getUnit();
                            killedUnit.setJustDied(true);
                        }
                        remainingMovement[0] -= map[currentPath.get(1).x][currentPath.get(1).y].getCost();
                        oldTile.setUnit(null);
                        newTile.setUnit(Unit.this);

                        for (int x = 0; x < map.length; x++) {
                            for (int y = 0; y < map[x].length; y++) {
                                if (map[x][y].getUnit() == Unit.this) {
                                    Unit.this.x = x;
                                    Unit.this.y = y;
                                }
                            }
                        }
                        currentPath.remove(0);
                    }
                }

                if(currentPath.size() <= 1){
                    currentPath = null;
                }
            }
        }, moveTime, moveTime);








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
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isJustDied() {
        return justDied;
    }

    public void setJustDied(boolean justDied) {
        this.justDied = justDied;
    }
}
