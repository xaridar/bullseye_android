package com.example.bullseye_android.games.turn_based;

import java.util.ArrayList;

public class Unit extends BoardActor implements MoveableUnit{


    private int x;
    private int y;

    private ArrayList<Node> currentPath = null;

    private int movespeed = 1;

    @Override
    public void update() {

    }

    @Override
    public void movement() {

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

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }
}
