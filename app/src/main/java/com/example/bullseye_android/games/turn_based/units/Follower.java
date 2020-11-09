package com.example.bullseye_android.games.turn_based.units;

import androidx.lifecycle.LifecycleOwner;

import com.example.bullseye_android.games.turn_based.Node;
import com.example.bullseye_android.games.turn_based.Owners;
import com.example.bullseye_android.games.turn_based.Pathfinder;
import com.example.bullseye_android.games.turn_based.Tile;
import com.example.bullseye_android.games.turn_based.TurnBasedActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Follower extends EnemyUnit {

    Unit closestUnit = null;

    Node[][] graph;

    public Follower(String name, int x, int y, String icon, int movespeed, Owners owner, Node[][] graph, Tile[][] board, LifecycleOwner ctx, TurnBasedActivity game) {
        super(name, x, y, icon, movespeed, owner, board, graph, ctx, game);
        this.graph = graph;
        this.board = board;
        closestUnit = getClosestUnit();
        ArrayList<Node> path = null;
        if(closestUnit!=null) path = Pathfinder.generatePathTo(x, y, closestUnit.x, closestUnit.y, graph, board, this);
        if (path != null) setCurrentPath(path);
    }

    @Override
    public void update() {
        super.update();
        closestUnit = getClosestUnit();
        ArrayList<Node> path = null;
        if(closestUnit!=null) path = Pathfinder.generatePathTo(x, y, closestUnit.x, closestUnit.y, graph, board, this);
        if (path != null) setCurrentPath(path);
    }

    @Override
    public void onMovementFinished(Tile[][] map, Node[][] graph) {
        super.onMovementFinished(map, graph);
    }

    private Unit getClosestUnit(){
        Unit[] closestUnit = {null};
        //There is no reason for this to be one line but I was going to die on this hill, so for reparations for my crimes against programming I will describe this in excruciating detail
        //Converts Tile[][] to a stream<Tile[]>
        Arrays.stream(board)
                //Converts the Stream<Tile[]> to Stream<Tile> of every value of board in sequential order
                .flatMap(Arrays::stream)
                //Removes tiles from stream that do not have a player unit
                .filter(tile -> tile.getUnit() != null && tile.getUnit().getOwner() == Owners.PLAYER)
                //Creates an ArrayList of tiles with player units
                .collect(Collectors.toCollection(ArrayList::new))
                //loops through tiles, comparing the length of the path to the current closest unit to the current tile's unit, and changing closestUnit to the lesser one
                .forEach(tile -> closestUnit[0] = closestUnit[0] == null
                        || Pathfinder.generatePathTo(x, y, closestUnit[0].x, closestUnit[0].y, graph, board, this).size()
                        < Pathfinder.generatePathTo(x, y, tile.getUnit().x, tile.getUnit().y, graph, board, this).size()
                        ? tile.getUnit()
                        : closestUnit[0]);
        return closestUnit[0];
    }
}
