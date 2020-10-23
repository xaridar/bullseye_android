package com.example.bullseye_android.games.turn_based;

public class Tile {
    // Tiles that have a cost or anything else i decide to add

    private String name;
    private int cost;
    private boolean isWalkable;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public void setWalkable(boolean walkable) {
        isWalkable = walkable;
    }
}
