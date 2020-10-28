package com.example.bullseye_android.games.turn_based;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> neighbours;
    public int x;
    public int y;

    public Node(){
        neighbours = new ArrayList<>();
    }

    public ArrayList<Node> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(ArrayList<Node> neighbours) {
        this.neighbours = neighbours;
    }

    public void addNeighbour(Node node){
        neighbours.add(node);
    }
    public void setNeighbor(Node node, int pos){
        neighbours.add(pos, node);
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
