package com.example.maze.game_play;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Maze {

    public Maze(InputStream fileName) {
        fillMaze(fileName);
    }

    public enum Square {
        WALL("#"), PATHWAY("."), FINISH("*"), START("0");

        private final String symbol;

        Square(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public static Square fromChar(Character ch)
                throws IllegalArgumentException {
            if (ch.toString().equals("#")) {
                return WALL;
            } else if (ch.toString().equals(".")) {
                return PATHWAY;
            } else if (ch.toString().equals("*")) {
                return FINISH;
            } else if (ch.toString().equals("0")) {
                return START;
            } else {
                throw new IllegalArgumentException("Invalid Character");
            }
        }
    }

    private Location[][] maze;
    private int[] dimensions = new int[2];

    void fillMaze(InputStream mazeFile) {
        Scanner input = new Scanner(mazeFile);

        dimensions[0] = input.nextInt();
        dimensions[1] = input.nextInt();

        maze = new Location[dimensions[0]][dimensions[1]];
        for (int row = 0; row < dimensions[0]; row++) {
            for (int col = 0; col < dimensions[1]; col++) {
                maze[row][col] = new Location(row, col, (input.next()
                        .toCharArray()[0]));
            }
        }
        input.close();

    }

    public List<Location> getNeighboringPaths(Location location) {
        ArrayList<Location> neighboringPaths = new ArrayList<Location>();

        if (canGetWest(location)) {
            neighboringPaths.add(getWest(location));
        }
        if (canGetNorth(location)) {
            neighboringPaths.add(getNorth(location));
        }
        if (canGetEast(location)) {
            neighboringPaths.add(getEast(location));
        }
        if (canGetSouth(location)) {
            neighboringPaths.add(getSouth(location));
        }

        return neighboringPaths;
    }

    public Location getStart() {
        Location start = null;
        for (Location[] row : maze) {
            for (Location l : row) {
                if (l.getType().equals(Square.START)) {
                    start = l;
                    break;
                }
            }
            if (start != null) {
                break;
            }
        }
        return start;
    }

    public Location getFinish() {
        Location finish = null;
        for (Location[] row : maze) {
            for (Location l : row) {
                if (l.getType().equals(Square.FINISH)) {
                    finish = l;
                    break;
                }
            }
            if (finish != null) {
                break;
            }
        }
        return finish;
    }

    public boolean isFinish(Location location) {
        return (location.getType().equals(Square.FINISH));
    }

    private boolean isWall(Location location) {
        return (location.getType().equals(Square.WALL));
    }

    public boolean canGetWest(Location l) {
        boolean canGo = true;
        int row = l.getRow();
        int col = l.getColumn();
        if (col == 0)
            canGo = false;
        if (canGo && isWall(maze[row][col - 1]))
            canGo = false;
        return canGo;
    }

    public Location getWest(Location l) {
        return maze[l.getRow()][l.getColumn() - 1];
    }

    public boolean canGetNorth(Location l) {
        boolean canGo = true;
        int row = l.getRow();
        int col = l.getColumn();
        if (col == 0)
            canGo = false;
        if (canGo && isWall(maze[row - 1][col]))
            canGo = false;
        return canGo;
    }

    public Location getNorth(Location l) {
        return maze[l.getRow() - 1][l.getColumn()];
    }

    public boolean canGetEast(Location l) {
        boolean canGo = true;
        int row = l.getRow();
        int col = l.getColumn();
        if (col == 0)
            canGo = false;
        if (canGo && isWall(maze[row][col + 1]))
            canGo = false;
        return canGo;
    }

    public Location getEast(Location l) {
        return maze[l.getRow()][l.getColumn() + 1];
    }

    public boolean canGetSouth(Location l) {
        boolean canGo = true;
        int row = l.getRow();
        int col = l.getColumn();
        if (col == 0)
            canGo = false;
        if (canGo && isWall(maze[row + 1][col]))
            canGo = false;
        return canGo;
    }

    public Location getSouth(Location l) {
        return maze[l.getRow() + 1][l.getColumn()];
    }

    public Location[][] getMaze() {
        return maze;
    }

    public Location getLocation(int row, int col) {
        return maze[row][col];
    }

    public int getWidth() {
        return dimensions[1];
    }

    public int getHeight() {
        return dimensions[0];
    }
}