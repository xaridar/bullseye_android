package com.example.bullseye_android.games.turn_based;

import com.example.bullseye_android.games.turn_based.units.Unit;

import java.util.ArrayList;

public class Tile extends BoardActor{
    // Tiles that have a cost or anything else i decide to add

    private int cost;
    private boolean isWalkable;
    private Unit unit;
    private int[] padding;

    public Tile(String name, int x, int y, String icon, int cost, boolean isWalkable, Unit unit){
        this.name = name;
        this.x = x;
        this.y = y;
        this.icon = icon;
        this.cost = cost;
        this.isWalkable = isWalkable;
        this.unit = unit;
        this.padding = new int[4];
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

    public int[] getPadding() {
        return padding;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.padding[0] = left;
        this.padding[1] = top;
        this.padding[2] = right;
        this.padding[3] = bottom;
    }
}
