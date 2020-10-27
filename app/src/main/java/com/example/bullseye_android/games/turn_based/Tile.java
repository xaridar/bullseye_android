package com.example.bullseye_android.games.turn_based;

import com.example.bullseye_android.games.turn_based.units.Unit;

import java.util.ArrayList;

public class Tile extends BoardActor{
    // Tiles that have a cost or anything else i decide to add

    private int cost;
    private boolean isWalkable;
    private Unit unit;

    public Tile(String name, int x, int y, ArrayList<Integer> tags, String icon, int cost, boolean isWalkable, Unit unit){
        this.name = name;
        this.x = x;
        this.y = y;
        this.tags = tags;
        this.icon = icon;
        this.cost = cost;
        this.isWalkable = isWalkable;
        this.unit = unit;
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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
