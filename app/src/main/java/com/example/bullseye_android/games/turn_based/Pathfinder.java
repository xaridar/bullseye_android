package com.example.bullseye_android.games.turn_based;

import android.util.Log;

import com.example.bullseye_android.games.turn_based.units.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Pathfinder {
    /**
     * Takes the unit that is moving, the target x position, and the target y position,
     * and generates a path using cost of tiles, and impassable tiles using the Dijkstra
     * Path-finding Algorithm.
     * {Credit to quill18creates on YouTube for the tutorial}
     *
     * @param x      X position of target tile
     * @param y      Y position of target tile
     * @param unit   Unit that is moving to tile
     */
    public static ArrayList<Node> generatePathTo(int sourceX, int sourceY, int x, int y, Node[][] graph, Tile[][] board, Unit unit) {

        // Clear unit's old path.
        if(unit.getOwner() == Owners.PLAYER) {
            unit.setCurrentPath(null);
        }

        if(!unitCanEnterTile(x, y, board, unit)) {
            // Clicked on tile that unit cannot walk on
            Log.i("TB",unit.getName() + ": returned unwalkable");
            return new ArrayList<>();
        }

        HashMap<Node, Float> dist = new HashMap<>();
        HashMap<Node, Node> prev = new HashMap<>();

        // Setup the list of nodes we haven't checked yet.
        ArrayList<Node> unvisited = new ArrayList<>();

        Node source = graph[sourceX][sourceY];

        Node target = graph[x][y];
        dist.put(source, (float) 0);
        prev.put(source, null);

        // Initialize everything to have INFINITY distance, since
        // we don't know any better right now. Also, it's possible
        // that some nodes CAN'T be reached from the source,
        // which would make INFINITY a reasonable value
        for (Node[] v : graph){
            for (Node b : v) {
                if(b != source) {

                    dist.put(b, Float.POSITIVE_INFINITY);
                    prev.put(b, null);

                }

                unvisited.add(b);

            }
        }

        while(unvisited.size() > 0) {
            // "u" is going to be the unvisited node with the smallest distance.
            Node u = null;

            for (Node possibleU : unvisited) {
                if(u == null || dist.get(possibleU) < dist.get(u)) {

                    u = possibleU;

                }
            }

            if(u == target) {
                break;	// Exit the while loop!
            }

            unvisited.remove(u);

            for (Node v : u.getNeighbours()) {
                //float alt = dist[u] + u.DistanceTo(v);
                float alt = dist.get(u) + costToEnterTile(v.x, v.y, board, unit);
                if( alt < dist.get(v) ) {
                    dist.put(v, alt);
                    prev.put(v, u);
                }
            }
        }

        // If we get there, the either we found the shortest route
        // to our target, or there is no route at ALL to our target.

        if(prev.get(target) == null) {
            // No route between our target and the source
            Log.i("PF",unit.getName() + ": no route");
            return new ArrayList<>();
        }

        ArrayList<Node> currentPath = new ArrayList<Node>();

        Node curr = target;

        // Step through the "prev" chain and add it to our path
        while(curr != null) {

            currentPath.add(curr);
            curr = prev.get(curr);
        }

        // Right now, currentPath describes a route from out target to our source
        // So we need to invert it!
        Collections.reverse(currentPath);
        return currentPath;
    }

    /**
     * Creates the node graph and sets node neighbors, so that the path-finding algorithm can make a path
     */
    public static Node[][] generatePathfindingGraph(int mapSizeX, int mapSizeY) {
        // Initialize the array
        Node[][] graph = new Node[mapSizeX][mapSizeY];

        // Initialize a Node for each spot in the array
        for(int x=0; x < mapSizeX; x++) {
            for(int y=0; y < mapSizeY; y++) {
                graph[x][y] = new Node();
                graph[x][y].setX(x);
                graph[x][y].setY(y);
            }
        }

        // Now that all the nodes exist, calculate their neighbours
        for(int x=0; x < mapSizeX; x++) {
            for(int y=0; y < mapSizeY; y++) {

                // This is the 4-way connection version:
                if(x > 0)
                    graph[x][y].addNeighbour( graph[x-1][y] );
                if(x < mapSizeX-1)
                    graph[x][y].addNeighbour( graph[x+1][y] );
                if(y > 0)
                    graph[x][y].addNeighbour( graph[x][y-1] );
                if(y < mapSizeY-1)
                    graph[x][y].addNeighbour( graph[x][y+1] );


                // This also works with 6-way hexes and n-way variable areas (like EU4)
            }
        }

        return graph;
    }

    /**
     * Determines if a unit can enter a tile.
     *
     * @param targetX   X position of tile to check
     * @param targetY   Y position of tile to check
     * @return          returns whether or not the unit can enter the tile
     */
    private static boolean unitCanEnterTile(int targetX, int targetY, Tile[][] board, Unit unit) {
        boolean output;
        if(board[targetX][targetY].isWalkable() && board[targetX][targetY].getUnit() == null){
//            Log.i("PF","there is nothing blocking target");
            output = true;
        }else if(board[targetX][targetY].getUnit() != null){
            if(board[targetX][targetY].getUnit().getOwner() != unit.getOwner()){
//                Log.i("PF","there is opposing unit");
                output = true;
            }else{
                if(board[targetX][targetY].getUnit() == unit){
//                    Log.i("PF","target is self");
                    output = true;
                }else {
                    Log.i("PF",unit.getName() + ": target is of same owner");
                    output = false;
                }
            }
        }else{
            Log.i("PF",unit.getName() + ": there is both no unit and a unit at the same time");
            output = false;
        }

        return output;
    }

    /**
     * Gets cost of specified tile.
     *
     * The path-finding algorithm finds the path of least cost to arrive to a destination.
     *
     * @param targetX   X position of tile to get cost from
     * @param targetY   Y position of tile to get cost from
     * @return          Returns path-finding cost of tile
     */
    public static float costToEnterTile(int targetX, int targetY, Tile[][] board, Unit unit) {

        Tile tile = board[ targetX ][ targetY ];

        if(!unitCanEnterTile(targetX, targetY, board, unit))
            return Float.POSITIVE_INFINITY;

        return (float) tile.getCost();

    }
}
