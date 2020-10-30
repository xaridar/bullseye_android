package com.example.bullseye_android.games.turn_based;


import java.util.ArrayList;

public class BoardActor {
    // Anything that is able to be put on the board.
    // Has tags that allow for different actors to have different abilities/effects (Slowed, poisoned, speed, etc.)
    public String name;
    public int x;
    public int y;
    public ArrayList<Integer> tags;
    public String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Integer> tags) {
        this.tags = tags;
    }

    public void addTag(int tag){
        tags.add(tag);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
